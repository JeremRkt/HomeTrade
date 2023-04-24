package com.isep.hometrade.web;

import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UserEntity());
        return "registration";
    }

    @PostMapping("/registration")
    public String processRegistration(@ModelAttribute("user") @Valid UserEntity user, BindingResult result, Model model) {
        UserEntity existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            result.rejectValue("email", null,"Cette adresse e-mail est déja associée à un compte !");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "/registration";
        }
        userService.saveUser(user);
        return "redirect:/registration?success";
    }

    @GetMapping("/welcomeLogged")
    public String welcomeLogged() {
        return "welcomeLogged";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

}
