package com.example.redis_practice.redis_chat;

import java.util.Scanner;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
public class ChatService implements MessageListener {

	private final RedisMessageListenerContainer redisMessageListenerContainer;
	private final StringRedisTemplate stringRedisTemplate;
	private final RedisTemplate<Object, Object> redisTemplate;

	public ChatService(RedisMessageListenerContainer redisMessageListenerContainer,
		StringRedisTemplate stringRedisTemplate, RedisTemplate<Object, Object> redisTemplate) {
		this.redisMessageListenerContainer = redisMessageListenerContainer;
		this.stringRedisTemplate = stringRedisTemplate;
		this.redisTemplate = redisTemplate;
	}

	public void enterChatRoom(String chatRoomName) {
		redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(chatRoomName));

		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.equals("q")) {
				System.out.println("::Chat room closed.");
				break;
			}

			redisTemplate.convertAndSend(chatRoomName, line);
		}

		redisMessageListenerContainer.removeMessageListener(this);
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		System.out.println("Message: " + message.toString());
	}
}
