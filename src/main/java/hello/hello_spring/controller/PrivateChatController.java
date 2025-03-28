package hello.hello_spring.controller;

import hello.hello_spring.domain.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class PrivateChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public PrivateChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 클라이언트가 "/app/chat/private"로 메시지를 보내면,
     * ChatMessage 객체의 receiverId(수신자 ID)를 이용해 해당 사용자에게만 전송합니다.
     *
     * 주의: 이 예제에서는 ChatMessage에 수신자 ID(receiverId) 필드가 있다고 가정합니다.
     */
    @MessageMapping("/chat/private")
    public void handlePrivateMessage(ChatMessage message) {
        // message.getReceiverId() 값은 사용자 인증정보(Principal)와 일치해야 합니다.
        // Spring WebSocket은 현재 연결된 사용자의 Principal을 기준으로 메시지를 전달합니다.
        messagingTemplate.convertAndSendToUser(
                message.getSender().getUsername(), // 예시: 수신자 대신 sender.username을 사용하고 있음. 실제 1:1 구현 시 receiver 필드를 사용해야 합니다.
                "/queue/private",
                message
        );
    }
}
