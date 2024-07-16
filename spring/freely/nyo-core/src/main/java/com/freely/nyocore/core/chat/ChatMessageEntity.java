package com.freely.nyocore.core.chat;

import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class ChatMessageEntity {

	private MessageType type;
	private String roomId;
	private String sender;
	private String message;

	public ChatMessageEntity(MessageType type, String roomId, String sender, String message) {
		this.type = type;
		this.roomId = roomId;
		this.sender = sender;
		this.message = message;
	}
}
