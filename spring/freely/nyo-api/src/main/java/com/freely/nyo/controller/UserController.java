package com.freely.nyo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freely.nyodomain.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;


	@GetMapping
	public ResponseEntity<Void> getProduct() {
		userService.testUser();
		return ResponseEntity.ok().build();
	}
}
