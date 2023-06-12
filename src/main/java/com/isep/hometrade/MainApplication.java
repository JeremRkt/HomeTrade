package com.isep.hometrade;

import com.isep.hometrade.map.UserDto;
import com.isep.hometrade.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(MainApplication.class, args);
		//UserService userService = ctx.getBean(UserService.class);
		//userService.saveUser(new UserDto("admin", "admin", "admin@admin", "admin"), true);
	}

}
