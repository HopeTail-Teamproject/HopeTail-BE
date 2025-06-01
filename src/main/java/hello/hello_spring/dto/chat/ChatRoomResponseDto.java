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

    @Schema(description = "member1 ID", example = "73")
    private final Long member1Id;                 // ★ 추가
    @Schema(description = "member1 username", example = "이민성")
    private final String member1Username;
    @Schema(description = "member1 이메일", example = "chocomin0211@hanyang.ac.kr")
    private final String member1Email;

    /* ───────── 회원 2 ───────── */
    @Schema(description = "member2 ID", example = "85")
    private final Long member2Id;                 // ★ 추가
    @Schema(description = "member2 username", example = "이규현")
    private final String member2Username;
    @Schema(description = "member2 이메일", example = "kks1234@gmail.com")
    private final String member2Email;

    @Schema(description = "방 생성 시각", example = "2025-05-23T15:48:49.712654")
    private final LocalDateTime createdAt;

    public ChatRoomResponseDto(ChatRoom r) {
        this.chatRoomId       = r.getId();
        this.member1Id        = r.getMember1().getId();          // ★
        this.member1Username  = r.getMember1().getUsername();
        this.member1Email = r.getMember1().getEmail();
        this.member2Id        = r.getMember2().getId();          // ★
        this.member2Username  = r.getMember2().getUsername();
        this.member2Email = r.getMember1().getEmail();
        this.createdAt        = r.getCreatedAt();
    }
}
