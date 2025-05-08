package hello.hello_spring.dto.chat;

import hello.hello_spring.domain.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomResponseDto {
    @Schema(description = "채팅방 ID", example = "1")
    private Long chatRoomId;

    @Schema(description = "첫 번째 채팅 참여자의 사용자명", example = "이민성")
    private String member1Username;

    @Schema(description = "두 번째 채팅 참여자의 사용자명", example = "이규현")
    private String member2Username;

    @Schema(description = "채팅방 생성 시각 (ISO 형식)", example = "2025-05-08T14:30:00")
    private LocalDateTime createdAt;

    // 생성자에서 엔티티 → DTO 변환
    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getId();
        this.member1Username = chatRoom.getMember1().getUsername(); // Member 엔티티에 getUsername()이 있다고 가정
        this.member2Username = chatRoom.getMember2().getUsername();
        this.createdAt = chatRoom.getCreatedAt();
    }
}
