package hello.hello_spring.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomRequestDto {
    @Schema(description = "채팅방 상대방의 이메일 주소", example = "hopetail@hopetail.com")
    private String partnerEmail;
}