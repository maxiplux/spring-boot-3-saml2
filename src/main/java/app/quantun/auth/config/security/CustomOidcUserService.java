package app.quantun.auth.config.security;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    @Autowired
    private RestTemplate restTemplate;

    private final OidcUserService delegate = new OidcUserService();

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        // Delegate to the default OidcUserService to load the user
        OidcUser oidcUser = delegate.loadUser(userRequest);

        // Extract claims from the ID token or user info
        Map<String, Object> claims = oidcUser.getClaims();
        List<MicrosoftGroup> azureGroups = getGroups(userRequest.getAccessToken().getTokenValue());

        // Extract roles and groups from the claims
        Set<GrantedAuthority> authorities = new HashSet<>();

//        List<String> roles = (List<String>) claims.get("roles");
        List<String> groups = azureGroups.stream().map(MicrosoftGroup::getDisplayName).toList();

        // Add roles to authorities
//        if (roles != null) {
//            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
//        }

        // Add groups to authorities
        if (groups != null) {
            groups.forEach(group -> authorities.add(new SimpleGrantedAuthority("ROLE_" + group)));
        }

        // Return a new DefaultOidcUser with the extracted authorities
        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }

    public List<MicrosoftGroup> getGroups(String accessToken) {
        // Build headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create request
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Call MS Graph
        ResponseEntity<JsonNode> response = restTemplate.exchange(
                "https://graph.microsoft.com/v1.0/groups",
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );

        // Parse and map response
        List<MicrosoftGroup> groups = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            JsonNode body = response.getBody();
            JsonNode valueArray = body.get("value");
            if (valueArray != null && valueArray.isArray()) {
                for (JsonNode groupNode : valueArray) {
                    MicrosoftGroup group = MicrosoftGroup.builder()
                            .id(groupNode.hasNonNull("id") ? groupNode.get("id").asText() : null)
                            .displayName(groupNode.hasNonNull("displayName") ? groupNode.get("displayName").asText() : null)
                            .description(groupNode.hasNonNull("description") ? groupNode.get("description").asText() : null)
                            .build();
                    groups.add(group);
                }
            }
        }

        return groups;
    }
}
