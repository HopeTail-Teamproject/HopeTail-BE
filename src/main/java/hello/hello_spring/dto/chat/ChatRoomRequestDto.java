package hello.hello_spring.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomRequestDto {
    private Long member1Id;
    private Long member2Id;
}