package hello.hello_spring.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageResponseDto {
    @Schema(description = "채팅방 ID", example = "1")
    private Long chatRoomId;

    @Schema(description = "보낸 사람의 username", example = "이민성")
    private String senderUsername;

    @Schema(description = "받는 사람의 username", example = "이규현")
    private String receiverUsername;

    @Schema(description = "메시지 내용", example = "안녕하세요.")
    private String content;

    @Schema(description = "생성 시각 (ISO 형식)", example = "2025-05-08T14:30:00")
    private LocalDateTime createdAt;  // 생성 시각
}
