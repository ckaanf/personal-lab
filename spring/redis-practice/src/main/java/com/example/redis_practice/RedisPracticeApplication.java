package com.example.redis_practice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.example.redis_practice.redis_chat.ChatService;

@EnableCaching
@SpringBootApplication
public class RedisPracticeApplication implements CommandLineRunner {

	private final ChatService chatService;

	public RedisPracticeApplication(ChatService chatService) {
		this.chatService = chatService;
	}

	public static void main(String[] args) {
		SpringApplication.run(RedisPracticeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("::Application started.");
		chatService.enterChatRoom("chat1");
	}
}
