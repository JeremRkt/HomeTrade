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
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserEntity());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") @Valid UserEntity user, BindingResult result, Model model) {
        UserEntity existingUser = userService.findByEmail(user.getEmail());
        if (existingUser == null) {
            result.rejectValue("email", null,"User is not already registered !");
        } else {
            if (!userService.isPasswordCorrect(existingUser, user.getPassword())) {
                result.rejectValue("password", null,"Incorrect password !");
            }
        }
        if (result.hasErrors()) {
            System.out.println(result);
            model.addAttribute("user", user);
            return "/login";
        }
        return "redirect:/welcomeLogged";
    }

}
