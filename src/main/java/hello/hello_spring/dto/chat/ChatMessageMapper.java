package hello.hello_spring.dto.chat;

import hello.hello_spring.domain.ChatMessage;

public final class ChatMessageMapper {

    private ChatMessageMapper() {}

    public static ChatMessageResponseDto toDto(ChatMessage m) {
        ChatMessageResponseDto dto = new ChatMessageResponseDto();
        dto.setId(m.getId());
        dto.setChatRoomId(m.getChatRoom().getId());

        dto.setSenderId(m.getSender().getId());
        dto.setSenderUsername(m.getSender().getUsername());
        dto.setSenderEmail(m.getSender().getEmail());

        dto.setReceiverId(m.getReceiver().getId());
        dto.setReceiverUsername(m.getReceiver().getUsername());
        dto.setReceiverEmail(m.getReceiver().getEmail());

        dto.setContent(m.getContent());
        dto.setCreatedAt(m.getCreatedAt());
        return dto;
    }
}
