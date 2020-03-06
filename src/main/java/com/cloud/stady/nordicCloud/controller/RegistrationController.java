package com.cloud.stady.nordicCloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cloud.stady.nordicCloud.exceptions.UserAlreadyExistsException;
import com.cloud.stady.nordicCloud.services.FileService;

@Controller
public class RegistrationController {

	@Autowired
	private FileService fileService;

	@GetMapping(path = "/registration.html")
	public String registrationPage() {
		return "registration";
	}

	@PostMapping(path = "/registration.html")
	public String registrationPage(@RequestParam(name = "login") String login, @RequestParam(name = "name") String name,
			@RequestParam(name = "password1") String password1, @RequestParam(name = "password2") String password2,
			Model model) throws UserAlreadyExistsException {
		if(password2.isEmpty() || password1.isEmpty() || login.isEmpty() || name.isEmpty()) {
			model.addAttribute("error1", true);
			return "registration";
		}
		if (!password2.equals(password1)) {
			model.addAttribute("error2", true);
			return "registration";

		}
		fileService.newUser(login, name, password1);
		
		return "redirect:/nordicCloud.html";
	}

}
