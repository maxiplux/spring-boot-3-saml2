package app.quantun.auth.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/custom-login")
    public String login() {
        return "login";
    }


}
