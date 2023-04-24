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

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

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
}
