package hello.hello_spring.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostUpdateRequestDto {
    @Schema(description = "게시글 제목", example = "유기견 골드 입양 후기입니다.")
    private String title;
    @Schema(description = "게시글 내용", example = "골드를 따듯한 보금자리로 데려왔습니다.")
    private String content;
    @Schema(description = "REVIEW(후기), DIARY(임보 일기) 둘 중 하나 선택")
    private String category;
}
