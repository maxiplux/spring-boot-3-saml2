package app.quantun.auth.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.saml2.provider.service.metadata.OpenSaml4MetadataResolver;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.metadata.Saml2MetadataResolver;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.RelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.authentication.OpenSaml4AuthenticationRequestResolver;
import org.springframework.security.saml2.provider.service.web.authentication.Saml2AuthenticationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Slf4j
@EnableMethodSecurity
public class SecurityConfig   {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    private CustomLogoutSuccessHandler logoutSuccessHandler;

//        @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//            http
//                    .csrf(AbstractHttpConfigurer::disable)
//                    .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers( "/css/**", "/js/**", "/iages/**", "/public/**","/media/**", "/custom-login", "/custom-logout",
//                                "/saml2/service-provider-metadata/**", "/saml2/authenticate/**", "/saml2/logout/**",
//                                "/saml2/metadata/**", "/saml2/redirect/**", "/saml2/slo/**","/saml2/**"
//                                ).permitAll()
//                            .requestMatchers("/dashboard").authenticated()
//                            .anyRequest().authenticated()
//                    )
//                    .saml2Login(saml2 -> saml2
//
//                            .successHandler(successHandler)
//                    )
//                    .logout(logout -> logout
//                            .logoutSuccessHandler(logoutSuccessHandler)
//                            .invalidateHttpSession(true)
//                            .deleteCookies("JSESSIONID")
//                    )
//                    .saml2Logout(Customizer.withDefaults());
//
//            return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Permit static resources and SAML endpoints
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/public/**",
                                "/media/**", "/custom-login","/login", "/custom-logout",
                                "/", "/error/**",
                                "/saml2/**").permitAll()
                        .requestMatchers("/dashboard").authenticated()
                        .anyRequest().authenticated()
                )
                .saml2Login(saml2 -> saml2
                        .loginPage("/custom-login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout/saml2/slo")
                        .logoutSuccessHandler(logoutSuccessHandler)
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/custom-login")
                        .deleteCookies("JSESSIONID")
                )
                .saml2Logout(Customizer.withDefaults());

        return http.build();
    }



    @Bean
    public Saml2MetadataResolver saml2MetadataResolver() {
        return new OpenSaml4MetadataResolver();
    }

//    @Bean
//    public Saml2AuthenticationRequestResolver authenticationRequestResolver(RelyingPartyRegistrationRepository registrations) {
//        RelyingPartyRegistrationResolver registrationResolver =   new DefaultRelyingPartyRegistrationResolver(registrations);
//        OpenSaml4AuthenticationRequestResolver authenticationRequestResolver =                new OpenSaml4AuthenticationRequestResolver(registrationResolver);
//        authenticationRequestResolver.setAuthnRequestCustomizer((context) -> context                .getAuthnRequest().setForceAuthn(true));
//        return authenticationRequestResolver;
//    }


}