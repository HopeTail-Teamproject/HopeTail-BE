package hello.hello_spring.service;

import hello.hello_spring.domain.jwt.token.TokenProvider;
import hello.hello_spring.domain.member.Member;
import hello.hello_spring.repository.ChatMessageRepository;
import hello.hello_spring.repository.ChatRoomRepository;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.domain.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository; // 이미 있을 것으로 가정
    private final TokenProvider tokenProvider;

    /* 채팅방 생성 (1:1 기준) */
    @Transactional
    public ChatRoom createChatRoom(HttpServletRequest request, String partnerEmail) {

        /* -------- (1) 내 e-mail 꺼내기 -------- */
        String bearerToken = request.getHeader("Authorization");
        String token = "";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        Claims claims = tokenProvider.getClaims(token);
        String myEmail = claims.getSubject();                // ★ 내 e-mail

        /* -------- (2) 이미 존재하는 방 찾기 -------- */
        Optional<ChatRoom> existing =
                chatRoomRepository.findByEmails(myEmail, partnerEmail); // ★ e-mail 두 개로 검색
        if (existing.isPresent()) return existing.get();

        /* -------- (3) 없으면 새로 만듦 -------- */
        Member me      = memberRepository.findByEmail(myEmail)
                .orElseThrow(() -> new RuntimeException("me not found"));
        Member partner = memberRepository.findByEmail(partnerEmail)
                .orElseThrow(() -> new RuntimeException("partner not found"));

        ChatRoom newRoom = new ChatRoom();
        newRoom.setMember1(me);
        newRoom.setMember2(partner);

        return chatRoomRepository.save(newRoom);
    }

    /**
     * 채팅 메시지 저장
     */
    public ChatMessage saveMessage(Long chatRoomId, String senderEmail, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        Member sender = memberRepository.findByEmail(senderEmail)
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
