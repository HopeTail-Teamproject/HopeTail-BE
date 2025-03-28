package hello.hello_spring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {
    private Long chatRoomId;
    private Long senderId;
    private String content;
}
