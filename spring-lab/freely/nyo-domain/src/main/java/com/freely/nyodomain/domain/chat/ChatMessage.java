package com.freely.nyodomain.domain.chat;

import com.freely.nyocore.core.chat.ChatMessageEntity;
import com.freely.nyocore.core.chat.MessageType;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatMessage {
	private MessageType type;
	private String roomId;
	private String sender;
	private String message;

	public static ChatMessage from (ChatMessageEntity chatMessageEntity) {
		return ChatMessage.builder()
			.type(chatMessageEntity.getType())
			.roomId(chatMessageEntity.getRoomId())
			.sender(chatMessageEntity.getSender())
			.message(chatMessageEntity.getMessage())
			.build();
	}

	public static ChatMessageEntity to (ChatMessage chatMessage) {
		return new ChatMessageEntity(
			chatMessage.getType(),
			chatMessage.getRoomId(),
			chatMessage.getSender(),
			chatMessage.getMessage()
		);
	}

	public void updateMessage(String message) {
		this.message = message;
	}
}
