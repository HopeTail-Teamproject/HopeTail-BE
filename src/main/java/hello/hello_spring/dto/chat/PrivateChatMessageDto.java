package hello.hello_spring.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivateChatMessageDto {
    @Schema(description = "채팅방 ID", example = "1")
    private Long chatRoomId;

    @Schema(description = "메시지 전송자의 사용자 ID", example = "1001")
    private Long senderId;

    @Schema(description = "메시지 수신자의 사용자 ID", example = "1002")
    private Long receiverId;

    @Schema(description = "채팅 메시지 내용", example = "안녕하세요! 반갑습니다.")
    private String content;
}