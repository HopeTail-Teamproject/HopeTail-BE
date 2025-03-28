package hello.hello_spring.dto;

import hello.hello_spring.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long memberId;
    private Long postId;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.memberId = comment.getMember().getId();
        this.postId = comment.getPost().getId();
    }
}
