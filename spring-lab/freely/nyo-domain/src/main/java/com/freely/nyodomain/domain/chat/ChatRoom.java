package com.freely.nyodomain.domain.chat;

import static com.freely.nyocore.core.chat.MessageType.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.freely.nyocore.core.chat.ChatRoomEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class ChatRoom {
	private String roomId;
	private String roomName;

	@JsonSerialize(using = WebSocketSessionSerializer.class)
	private Set<WebSocketSession> sessions = new HashSet<>();

	public ChatRoom() {

	}

	@Builder
	public ChatRoom(String roomId, String roomName) {
		this.roomId = roomId;
		this.roomName = roomName;
	}

	public static ChatRoom from(ChatRoomEntity chatRoomEntity) {
		return ChatRoom.builder()
			.roomId(chatRoomEntity.getRoomId())
			.roomName(chatRoomEntity.getRoomName())
			.build();

	}

	public static ChatRoomEntity to(ChatRoom chatRoom) {
		return new ChatRoomEntity(
			chatRoom.roomId,
			chatRoom.roomName
		);
	}

	public void handleAction(WebSocketSession session, ChatMessage chatMessage) {
		if (chatMessage.getType().equals(ENTER)) {
			sessions.add(session);
			chatMessage.updateMessage(chatMessage.getSender() + "님이 입장하셨습니다. ");
		}
		sendMessage(chatMessage);
	}

	public <T> void sendMessage(T message) {
		sessions.parallelStream().forEach(session -> sendMessage(session, message, new ObjectMapper()));
	}

	public <T> void sendMessage(WebSocketSession session, T message, ObjectMapper objectMapper) {
		try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	static class WebSocketSessionSerializer extends JsonSerializer<Set<WebSocketSession>> {
		@Override
		public void serialize(Set<WebSocketSession> sessions, JsonGenerator gen, SerializerProvider serializers) throws
			IOException {
			Set<String> sessionIds = sessions.stream()
				.map(WebSocketSession::getId)
				.collect(Collectors.toSet());
			gen.writeObject(sessionIds);
		}
	}
}
