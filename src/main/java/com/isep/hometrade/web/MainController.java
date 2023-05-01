package com.isep.hometrade.web;

import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.service.HouseService;
import com.isep.hometrade.service.UserService;
import com.isep.hometrade.util.HouseDto;
import com.isep.hometrade.util.UserDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    private final UserService userService;

    private final HouseService houseService;

    @Autowired
    public MainController(UserService userService, HouseService houseService) {
        this.userService = userService;
        this.houseService = houseService;
    }

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
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @PostMapping("/registration")
    public String processRegistration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        UserEntity existingUser = userService.findUserByEmail(userDto.getEmail());
        if (existingUser != null) {
            result.rejectValue("email", null,"L'adresse e-mail saies est déjà associée à un compte !");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/registration";
        }
        userService.saveUser(userDto);
        return "redirect:/registration?success";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        UserEntity user = userService.findUserByEmail(authentication.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/add-house")
    public String addHouse(Model model) {
        model.addAttribute("house", new HouseDto());
        return "add-house";
    }

    @PostMapping("/add-house")
    public String processAddHouse(@Valid @ModelAttribute("house") HouseDto houseDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("house", houseDto);
            return "/add-house";
        }
        houseService.saveHouse(houseDto);
        return "redirect:/add-house?success";
    }

}
