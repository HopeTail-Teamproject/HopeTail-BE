package hello.hello_spring.controller;

import hello.hello_spring.domain.ChatRoom;
import hello.hello_spring.dto.chat.ChatRoomRequestDto;
import hello.hello_spring.dto.chat.ChatRoomResponseDto;
import hello.hello_spring.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;

    @Operation(summary = "채팅방 생성")
    @PostMapping
    public ResponseEntity<ChatRoomResponseDto> createRoom(@RequestBody ChatRoomRequestDto dto, HttpServletRequest request) {

        ChatRoom room = chatService.createChatRoom(request,
                dto.getPartnerEmail()); // 상대 e-mail 한 개
        return ResponseEntity.ok(new ChatRoomResponseDto(room));
    }

    @Operation(summary = "채팅방 상세 조회")
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> getChatRoom(@PathVariable Long roomId) {

        ChatRoom room = chatService.getChatRoom(roomId);

        return ResponseEntity.ok(new ChatRoomResponseDto(room));
    }
}
