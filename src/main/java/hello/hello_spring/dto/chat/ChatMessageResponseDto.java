package hello.hello_spring.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ChatMessageResponseDto {

    /* ────────── 식별 정보 ────────── */
    @Schema(description = "메시지 PK", example = "42")
    private Long id;                               // ★ 신규

    @Schema(description = "채팅방 ID", example = "1")
    private Long chatRoomId;

    /* ────────── 발신자 ────────── */
    @Schema(description = "보낸 사람 ID",  example = "1001")
    private Long senderId;                         // ★ 신규
    @Schema(description = "보낸 사람 username", example = "이민성")
    private String senderUsername;
    @Schema(description = "보낸 사람 이메일", example = "chocomin0211@hanyang.ac.kr")
    private String senderEmail;

    /* ────────── 수신자 ────────── */
    @Schema(description = "받는 사람 ID",  example = "1002")
    private Long receiverId;                       // ★ 신규
    @Schema(description = "받는 사람 username", example = "이규현")
    private String receiverUsername;
    @Schema(description = "받는 사람 이메일", example = "kks1234@gmail.com")
    private String receiverEmail;

    /* ────────── 내용/메타 ────────── */
    @Schema(description = "메시지 내용", example = "안녕하세요.")
    private String content;

    @Schema(description = "생성 시각(ISO-8601)", example = "2025-05-08T14:30:00")
    private LocalDateTime createdAt;
}
