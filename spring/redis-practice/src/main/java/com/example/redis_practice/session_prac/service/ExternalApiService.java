package com.example.redis_practice.session_prac.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiService {

	public String getUserName(String userId) {
		// 외부 서비스나 DB 호출
		// 시간이 좀 걸림
		try {
			Thread.sleep(500);

		} catch (InterruptedException e) {

		}

		System.out.println("Getting user name from other service...");

		if (userId.equals("A")) {
			return "Adam";
		}

		if (userId.equals("B")) {
			return "Bob";
		}

		return "";
	}

	// 일반적으로 spring 제공 cache가 좋음
	@Cacheable(cacheNames = "userAgeCache", key = "#userId")
	public int getUserAge(String userId) {
		try {
			Thread.sleep(500);

		} catch (InterruptedException e) {

		}

		System.out.println("Getting user age from other service...");

		if (userId.equals("A")) {
			return 28;
		}

		if (userId.equals("B")) {
			return 32;
		}

		return 0;
	}
}

