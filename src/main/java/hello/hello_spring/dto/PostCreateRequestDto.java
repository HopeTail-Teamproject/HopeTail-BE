package hello.hello_spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostCreateRequestDto {
    @Schema(description = "게시글 제목")
    private String title;
    @Schema(description = "게시글 내용")
    private String content;
    @Schema(description = "카테고리 (REVIEW, DIARY)")
    private String category;
    @Schema(description = "작성자 memberId (임시로 받는다고 가정)")
    private Long memberId;
}
