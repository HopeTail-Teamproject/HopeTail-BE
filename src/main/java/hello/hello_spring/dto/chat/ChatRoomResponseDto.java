package hello.hello_spring.dto.chat;

import hello.hello_spring.domain.ChatRoom;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomResponseDto {
    private Long chatRoomId;
    private String member1Username;
    private String member2Username;
    private LocalDateTime createdAt;

    // 생성자에서 엔티티 → DTO 변환
    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getId();
        this.member1Username = chatRoom.getMember1().getUsername(); // Member 엔티티에 getUsername()이 있다고 가정
        this.member2Username = chatRoom.getMember2().getUsername();
        this.createdAt = chatRoom.getCreatedAt();
    }
}
