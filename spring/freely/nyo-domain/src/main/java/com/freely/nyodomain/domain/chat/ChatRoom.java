package com.freely.nyodomain.domain.chat;

import static com.freely.nyocore.core.chat.MessageType.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.freely.nyocore.core.chat.ChatRoomEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Builder
@Slf4j
public class ChatRoom {
	private String roomId;
	private String roomName;
	private final Set<WebSocketSession> sessions = new HashSet<>();

	public static ChatRoom from(ChatRoomEntity chatRoomEntity) {
		return ChatRoom.builder()
			.roomId(chatRoomEntity.getRoomId())
			.roomName(chatRoomEntity.getRoomName())
			.build();
	}

	public static ChatRoomEntity to(ChatRoom chatRoom) {
		return new ChatRoomEntity(
			chatRoom.roomId,
			chatRoom.roomName,
			chatRoom.sessions
		);
	}

	public void handleAction(WebSocketSession session, ChatMessage chatMessage) {
		if (chatMessage.getType().equals(ENTER)) {
			sessions.add(session);
			chatMessage.updateMessage(chatMessage.getSender() + "님이 입장하셨습니다. ");
		}
		sendMessage(session, chatMessage);
	}

	public <T> void sendMessage(WebSocketSession session, T message) {
		try {
			session.sendMessage((WebSocketMessage<?>)message);

		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
