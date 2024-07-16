package com.freely.nyocore.core.chat;

import java.util.HashSet;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatRoomEntity {
	private String roomId;
	private String roomName;
	private Set<?> sessions = new HashSet<>();

	public ChatRoomEntity(String roomId, String roomName, Set<?> sessions) {
		this.roomId = roomId;
		this.roomName = roomName;
		this.sessions = sessions;
	}
}
