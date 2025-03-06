package com.freely.nyochat.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freely.nyodomain.domain.chat.ChatRoom;
import com.freely.nyodomain.service.chat.ChatService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
	private final ChatService chatService;

	@PostMapping
	public ChatRoom createRoom(@RequestBody ChatRoom chatRoom) {
		return chatService.createRoom(chatRoom.getRoomName());
	}

	@GetMapping
	public List<ChatRoom> findAllRoom() {
		return chatService.findAllRoom();
	}

	@GetMapping("/{roomId}")
	public ChatRoom findRoomById(@PathVariable String roomId) {
		return chatService.findRoomById(roomId);
	}
}
