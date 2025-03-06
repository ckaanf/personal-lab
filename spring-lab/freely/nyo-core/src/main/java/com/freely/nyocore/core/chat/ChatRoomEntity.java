package com.freely.nyocore.core.chat;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;

@Getter
public class ChatRoomEntity {
	// @GeneratedValue(strategy = GenerationType.UUID)
	private String roomId;
	private String roomName;
	private Set<?> sessions = new HashSet<>();

	@Builder
	public ChatRoomEntity(String roomId, String roomName) {
		this.roomId = roomId;
		this.roomName = roomName;
	}
}
