package hello.hello_spring.service;

import hello.hello_spring.domain.member.Member;
import hello.hello_spring.repository.ChatMessageRepository;
import hello.hello_spring.repository.ChatRoomRepository;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository; // 이미 있을 것으로 가정

    /**
     * 채팅방 생성 (1:1 기준)
     * - 만약 이미 member1, member2가 있는 채팅방이 있다면 재사용
     */
    public ChatRoom createChatRoom(Long member1Id, Long member2Id) {
        Member member1 = memberRepository.findById(member1Id)
                .orElseThrow(() -> new RuntimeException("member1 not found"));
        Member member2 = memberRepository.findById(member2Id)
                .orElseThrow(() -> new RuntimeException("member2 not found"));

        // 이미 둘 사이의 채팅방이 있는지 확인
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByMember1AndMember2(member1, member2);
        if (existingRoom.isPresent()) {
            return existingRoom.get();
        }

        // 없다면 새로 생성
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setMember1(member1);
        chatRoom.setMember2(member2);
        return chatRoomRepository.save(chatRoom);
    }

    /**
     * 채팅 메시지 저장
     */
    public ChatMessage saveMessage(Long chatRoomId, Long senderId, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender);
        chatMessage.setContent(content);

        return chatMessageRepository.save(chatMessage);
    }

    public ChatRoom getChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found: " + roomId));
    }
    /**
     * 채팅 메시지 조회
     */
    public List<ChatMessage> getMessages(Long chatRoomId) {
        return chatMessageRepository.findAllByChatRoomIdOrderByCreatedAtAsc(chatRoomId);
    }
}
