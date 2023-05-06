package com.isep.hometrade.web;

import com.isep.hometrade.business.HouseEntity;
import com.isep.hometrade.business.UserEntity;
import com.isep.hometrade.service.HouseService;
import com.isep.hometrade.service.UserService;
import com.isep.hometrade.map.HouseDto;
import com.isep.hometrade.map.UserDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

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
        Set<HouseEntity> houses = userService.findHousesByEmail(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("houses", houses);
        return "profile";
    }

    @GetMapping("/add-house")
    public String addHouse(Model model) {
        model.addAttribute("house", new HouseDto());
        return "add-house";
    }

    @PostMapping("/add-house")
    public String processAddHouse(@Valid @ModelAttribute("house") HouseDto houseDto, Authentication authentication, BindingResult result, Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("house", houseDto);
            return "/add-house";
        }
        UserEntity user = userService.findUserByEmail(authentication.getName());
        houseService.saveHouse(houseDto,user);
        return "redirect:/add-house?success";
    }

}
