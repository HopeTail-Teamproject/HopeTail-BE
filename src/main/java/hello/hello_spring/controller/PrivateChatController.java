package hello.hello_spring.controller;

import hello.hello_spring.dto.PrivateChatMessageDto;
import hello.hello_spring.dto.ChatMessageResponseDto;
import hello.hello_spring.domain.ChatMessage;
import hello.hello_spring.domain.ChatRoom;
import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.ChatMessageRepository;
import hello.hello_spring.repository.ChatRoomRepository;
import hello.hello_spring.repository.MemberRepository;
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

    @MessageMapping("/chat/private")
    public void handlePrivateMessage(PrivateChatMessageDto dto) {
        // 1) DTO에서 정보 추출
        Long chatRoomId = dto.getChatRoomId();
        Long senderId = dto.getSenderId();
        Long receiverId = dto.getReceiverId();
        String content = dto.getContent();

        // 2) DB에서 엔티티 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found: " + chatRoomId));
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found: " + senderId));
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found: " + receiverId));

        // 3) ChatMessage 객체 구성 및 저장
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender);
        chatMessage.setReceiver(receiver);
        chatMessage.setContent(content);

        chatMessageRepository.save(chatMessage);

        // 4) 응답 DTO에 값 채우기 (세션 내에서 값을 미리 꺼내기)
        ChatMessageResponseDto responseDto = new ChatMessageResponseDto();
        responseDto.setChatRoomId(chatRoom.getId());
        responseDto.setSenderUsername(sender.getUsername());
        responseDto.setReceiverUsername(receiver.getUsername());
        responseDto.setContent(chatMessage.getContent());
        responseDto.setCreatedAt(chatMessage.getCreatedAt());

        // 5) receiver의 username을 기준으로 /queue/private로 전송
        messagingTemplate.convertAndSendToUser(
                receiver.getUsername(),
                "/queue/private",
                responseDto
        );
    }
}
