package app.quantun.auth.config.security;


import app.quantun.auth.models.CustomUserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {


            //log.info("User successfully authenticated: {}", userDTO.getUsername());


            response.sendRedirect("/app/dashboard"); // Redirect to dashboard after login


    }


    private String getSingleAttribute(Map<String, List<Object>> attributes, String key) {
        List<Object> values = attributes.get(key);
        return values != null && !values.isEmpty() ? values.get(0).toString() : null;
    }
}


