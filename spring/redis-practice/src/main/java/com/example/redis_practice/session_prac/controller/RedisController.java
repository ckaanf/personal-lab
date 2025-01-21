package com.example.redis_practice.session_prac.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RedisController {

	private final StringRedisTemplate redisTemplate;

	public RedisController(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@GetMapping("/hello")
	public String hello() {
		return "Hello World";
	}
	// 과일 가져오기
	@GetMapping("/getFruit")
	public String getFruit() {
		return redisTemplate.opsForValue().get("fruit");
	}
	//과일 설정
	@GetMapping("/setFruit")
	public String setFruit(@RequestParam String name) {
		ValueOperations<String, String> ops = redisTemplate.opsForValue();
		ops.set("fruit", name);

		return "saved.";
	}
}
