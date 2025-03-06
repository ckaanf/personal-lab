package com.freely.nyodomain.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CasherManager {
	private final ProductService productService;
	private final UserService userService;
}
