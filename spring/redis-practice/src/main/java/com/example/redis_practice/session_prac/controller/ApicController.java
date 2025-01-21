package com.example.redis_practice.session_prac.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.redis_practice.session_prac.dto.UserProfile;
import com.example.redis_practice.session_prac.service.UserService;

@RestController
public class ApicController {

	private final UserService userService;

	public ApicController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/users/{userId}/profile")
	public UserProfile getUserProfile(@PathVariable String userId) {
		return userService.getUserProfile(userId);
	}

}
