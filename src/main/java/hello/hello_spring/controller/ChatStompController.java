package hello.hello_spring.controller;

import hello.hello_spring.domain.ChatMessage;
import hello.hello_spring.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import hello.hello_spring.controller.dto.ChatMessageDto;

@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final ChatService chatService;

    /**
     * 클라이언트가 "/app/chat/message"로 메시지를 전송하면 이 메서드가 호출됨.
     * @param chatMessageDto - 채팅방 ID, sender ID, 메시지 내용 등을 포함한 DTO
     * @return 저장된 ChatMessage를 반환하면, @SendTo에 지정한 경로(/topic/chat)로 브로드캐스트됨
     */
    @MessageMapping("/chat/message")
    @SendTo("/topic/chat")
    public ChatMessage sendMessage(ChatMessageDto chatMessageDto) {
        // 서비스에서 메시지를 저장하고, 저장된 ChatMessage를 반환
        ChatMessage savedMessage = chatService.saveMessage(
                chatMessageDto.getChatRoomId(),
                chatMessageDto.getSenderId(),
                chatMessageDto.getContent()
        );
        return savedMessage;
    }
}
