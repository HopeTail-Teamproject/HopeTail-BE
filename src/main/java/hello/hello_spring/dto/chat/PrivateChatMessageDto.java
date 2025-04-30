package hello.hello_spring.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivateChatMessageDto {
    private Long chatRoomId;
    private Long senderId;
    private Long receiverId;
    private String content;
}