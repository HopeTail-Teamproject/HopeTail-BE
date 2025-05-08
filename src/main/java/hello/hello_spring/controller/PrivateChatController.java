package hello.hello_spring.controller;

import hello.hello_spring.dto.chat.PrivateChatMessageDto;
import hello.hello_spring.dto.chat.ChatMessageResponseDto;
import hello.hello_spring.domain.ChatMessage;
import hello.hello_spring.domain.ChatRoom;
import hello.hello_spring.domain.member.Member;
import hello.hello_spring.repository.ChatMessageRepository;
import hello.hello_spring.repository.ChatRoomRepository;
import hello.hello_spring.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class PrivateChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public PrivateChatController(SimpMessagingTemplate messagingTemplate,
                                 ChatRoomRepository chatRoomRepository,
                                 MemberRepository memberRepository,
                                 ChatMessageRepository chatMessageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Operation(
            summary = "개인 채팅 메시지 전송",
            description = "지정된 채팅방에 개인 메시지를 전송합니다. 메시지는 데이터베이스에 저장되며, 이후 WebSocket을 통해 실시간으로 수신자에게 전달됩니다.",
            tags = {"Private Chat", "Messaging"}
    )
    @MessageMapping("/chat/private")
    public void handlePrivateMessage(PrivateChatMessageDto dto) {
        Long chatRoomId = dto.getChatRoomId();
        Long senderId   = dto.getSenderId();
        Long receiverId = dto.getReceiverId();
        String content  = dto.getContent();

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found: " + chatRoomId));
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found: " + senderId));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found: " + receiverId));

        // 메시지 DB 저장
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender);
        chatMessage.setReceiver(receiver);
        chatMessage.setContent(content);
        chatMessageRepository.save(chatMessage);

        // 응답 DTO
        ChatMessageResponseDto responseDto = new ChatMessageResponseDto();
        responseDto.setChatRoomId(chatRoom.getId());
        responseDto.setSenderUsername(sender.getUsername());
        responseDto.setReceiverUsername(receiver.getUsername());
        responseDto.setContent(chatMessage.getContent());
        responseDto.setCreatedAt(chatMessage.getCreatedAt());

        // ================ 가장 중요한 부분 ================
        // Topic으로 전송 -> "/topic/chatroom/{chatRoomId}"
        messagingTemplate.convertAndSend(
                "/topic/chatroom/" + chatRoomId,
                responseDto
        );
        // ===================================================
    }
}
