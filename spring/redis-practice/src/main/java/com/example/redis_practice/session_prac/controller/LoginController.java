package com.example.redis_practice.session_prac.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

	@GetMapping("/login")
	public String login(HttpSession sesssion, @RequestParam String name) {
		sesssion.setAttribute("name", name);
		return "saved.";
	}

	@GetMapping("/my-name")
	public String myName(HttpSession sesssion) {
		return (String) sesssion.getAttribute("name");
	}
}
