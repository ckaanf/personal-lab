package com.freely.nyodomain.service.chat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.freely.nyocore.core.chat.ChatRoomEntity;
import com.freely.nyodomain.domain.chat.ChatRoom;

import jakarta.annotation.PostConstruct;

@Service
public class ChatService {

	private Map<String, ChatRoom> chatRooms;

	@PostConstruct
	private void init() {
		chatRooms = new LinkedHashMap<>();
	}

	public List<ChatRoom> findAllRoom() {
		return new ArrayList<>(chatRooms.values());
	}

	public ChatRoom findRoomById(String roomId){
		return chatRooms.get(roomId);
	}

	public ChatRoom createRoom(String name) {
		String randomId = UUID.randomUUID().toString();

		ChatRoom chatRoom = ChatRoom.builder()
			.roomId(randomId)
			.roomName(name)
			.build();
		chatRooms.put(randomId, chatRoom);
		return chatRoom;
	}
}
