package hello.hello_spring.controller;

import hello.hello_spring.domain.ChatRoom;
import hello.hello_spring.dto.ChatRoomRequestDto;
import hello.hello_spring.dto.ChatRoomResponseDto;
import hello.hello_spring.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;

    /**
     * 채팅방 생성 (POST /chatrooms)
     * - member1Id, member2Id로 새로운 1:1 채팅방 생성
     * - 이미 존재하면 그 방을 재사용
     */
    @PostMapping
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(@RequestBody ChatRoomRequestDto requestDto) {

        // Service를 통해 DB에 채팅방 생성(또는 재사용)
        ChatRoom newRoom = chatService.createChatRoom(
                requestDto.getMember1Id(),
                requestDto.getMember2Id()
        );

        // 엔티티를 DTO로 변환
        ChatRoomResponseDto responseDto = new ChatRoomResponseDto(newRoom);

        // 200 OK + response body
        return ResponseEntity.ok(responseDto);
    }

    /**
     * (선택) 채팅방 상세 조회 API (GET /chatrooms/{roomId})
     * 필요하면 추가로 작성
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponseDto> getChatRoom(@PathVariable Long roomId) {
        // ChatService에서 roomId로 ChatRoom 엔티티를 찾는다
        ChatRoom foundRoom = chatService.getChatRoom(roomId);
        // 참고: chatService.getChatRoom(...) 메서드는
        // repository.findById(roomId).orElseThrow(...) 등으로 구현

        // DTO 변환
        ChatRoomResponseDto responseDto = new ChatRoomResponseDto(foundRoom);
        return ResponseEntity.ok(responseDto);
    }
}
