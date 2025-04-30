package hello.hello_spring.controller;

import hello.hello_spring.dto.comment.CommentCreateRequestDto;
import hello.hello_spring.dto.comment.CommentResponseDto;
import hello.hello_spring.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성 (게시글 ID 필요)")
    @PostMapping("/posts/{postId}/comments")
    public CommentResponseDto createComment(@PathVariable Long postId,
                                            @RequestBody CommentCreateRequestDto dto) {
        return commentService.createComment(postId, dto);
    }

    @Operation(summary = "특정 게시글의 댓글 목록 조회")
    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponseDto> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }

    @Operation(summary = "댓글 단건 조회")
    @GetMapping("/comments/{commentId}")
    public CommentResponseDto getComment(@PathVariable Long commentId) {
        return commentService.getComment(commentId);
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
