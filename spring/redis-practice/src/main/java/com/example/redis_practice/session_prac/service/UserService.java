package com.example.redis_practice.session_prac.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.example.redis_practice.session_prac.dto.UserProfile;

@Service
public class UserService {

	private final ExternalApiService externalApiService;
	private final StringRedisTemplate redisTemplate;

	public UserService(ExternalApiService externalApiService, StringRedisTemplate redisTemplate) {
		this.externalApiService = externalApiService;
		this.redisTemplate = redisTemplate;
	}

	public UserProfile getUserProfile(String userId) {
		String userName = null;
		ValueOperations<String, String> ops = redisTemplate.opsForValue();
		String cacheName = ops.get("nameKey:" + userId);

		// Cache-Aside Pattern
		if (cacheName != null) {
			userName = cacheName;
		} else {
			userName = externalApiService.getUserName(userId);
			ops.set("nameKey:" + userId, userName, 5, TimeUnit.SECONDS);
		}

		// String userName = externalApiService.getUserName(userId);
		int userAge = externalApiService.getUserAge(userId);

		return new UserProfile(userName, userAge);
	}
}
