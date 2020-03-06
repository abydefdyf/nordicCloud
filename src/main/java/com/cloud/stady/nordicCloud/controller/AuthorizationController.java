package com.cloud.stady.nordicCloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cloud.stady.nordicCloud.exceptions.NotFoundUserException;
import com.cloud.stady.nordicCloud.exceptions.WrongPasswordException;
import com.cloud.stady.nordicCloud.services.FileService;

@Controller
public class AuthorizationController {
	
	@Autowired
	private FileService fileService;
	
	@GetMapping(path = "/authorization.html")
	public String loginPage() {
		return "authorization";
	}
	
	@PostMapping(path = "/authorization.html")
	public String authorizationPage(@RequestParam(name = "login") String login, @RequestParam(name = "password") String password, Model model ) throws WrongPasswordException, NotFoundUserException {
		var user = fileService.getUser(login, password);
		if(user == null) {
			model.addAttribute("error", true);
			return "authorization";
		}
		if(!user.getPassword().equals(password)) {
			model.addAttribute("error", true);
			return "authorization";
		}
		model.addAttribute("user", user);
		return "authorization";
	}
	

}
