package hello.hello_spring.dto.chat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageResponseDto {
    private Long chatRoomId;          // 채팅방 ID
    private String senderUsername;    // 보낸 사람의 username
    private String receiverUsername;  // 받는 사람의 username
    private String content;           // 메시지 내용
    private LocalDateTime createdAt;  // 생성 시각
}
