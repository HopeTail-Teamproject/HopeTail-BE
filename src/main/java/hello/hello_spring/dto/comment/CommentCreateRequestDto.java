package hello.hello_spring.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentCreateRequestDto {
    @Schema(description = "댓글 내용")
    private String content;
//    @Schema(description = "작성자 memberId (임시)")
//    private Long postId;
    // postId는 URL 경로에서 받을 예정 (/api/posts/{postId}/comments)
}
