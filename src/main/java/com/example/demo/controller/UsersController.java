package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.entities.*;
import com.example.demo.services.UsersService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsersController {
	
	@Autowired
	UsersService service;
	
	@PostMapping("/register")
	public String addUser(@ModelAttribute Users user,Model model) {
		String msg;
		boolean user_status = service.emailExists(user.getEmail());
		if(user_status == false) {
			msg = service.addUser(user);
		}else {
			msg = "User already exists";
			model.addAttribute("msg",msg);
			return "registration";
		}
		model.addAttribute("msg",msg);
		return "home";
	}
	
	@PostMapping("/validate")
	public String validate(@RequestParam("email") String email, @RequestParam("password") String password,Model model,HttpSession session) {
		String msg;
		if(service.emailExists(email) == true) {
			if(service.validateUser(email,password) == true) {
				String role = service.getRole(email);
				
				session.setAttribute("email", email);
				
				if(role.equals("admin")) {
					return "adminHome";
				}else {
					return "customerHome";
				}
			}else {
				msg = "Incorrect Password";
				model.addAttribute("msg", msg);
				return "login";
			}
		}else {
			msg = "Email not register";
			model.addAttribute("msg", msg);
			return "login";
		}
	}
	
	
//	@GetMapping("/pay")
//	public	String pay(@RequestParam String email) {
//		boolean paymentStatus = false;
//		if(paymentStatus == true) {
//			Users user = service.getUser(email);
//			user.setPremium(true);
//			service.updateUser(user);
//		}
//		return "login";
//	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}
	
}
