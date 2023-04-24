package com.isep.hometrade.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeLoggedController {
    @GetMapping("/welcomeLogged")
    public String welcomeLogged() {
        return "welcomeLogged";
    }
}
