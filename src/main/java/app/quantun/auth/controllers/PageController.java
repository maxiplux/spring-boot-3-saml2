package app.quantun.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.saml2.provider.service.metadata.Saml2MetadataResolver;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/extras")
class PageController {


    @Autowired
    private RelyingPartyRegistrationRepository relyingPartyRegistrationRepository;

    @Autowired
    private Saml2MetadataResolver saml2MetadataResolver;

    @GetMapping("/saml2/service-provider-metadata/{registrationId}")
    public ResponseEntity<String> metadata(@PathVariable String registrationId) {
        RelyingPartyRegistration registration =
                relyingPartyRegistrationRepository.findByRegistrationId(registrationId);
        //http://localhost:8080/app/extras/saml2/service-provider-metadata/pingone/
        if (registration == null) {
            return ResponseEntity.notFound().build();
        }

        String metadata = saml2MetadataResolver.resolve(registration);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(metadata);
    }

}
