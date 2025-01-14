package app.quantun.auth.controllers;

import app.quantun.auth.models.CustomUserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class SamlController {

    //@Autowired
    //private RelyingPartyRegistrationRepository relyingPartyRegistrationRepository;

    @GetMapping("/home")
    @ResponseBody
    public Map<String, Object> home(@AuthenticationPrincipal Saml2AuthenticatedPrincipal principal, Model model) {
        return Map.of(
                "username", principal.getName(),
                "attributes", principal.getAttributes(),
                "authenticated", true
        );
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String dashboard(HttpSession session) {
        CustomUserDTO userDTO = (CustomUserDTO) session.getAttribute("USER_DTO");
        return "dashboard";
    }


    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseBody
    public String greeting(HttpSession session) {
        CustomUserDTO userDTO = (CustomUserDTO) session.getAttribute("USER_DTO");
        return "I'm SAML user!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseBody
    public String admin() {
        return "I'm SAML admin!";
    }


}
