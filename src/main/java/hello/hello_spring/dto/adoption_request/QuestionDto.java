package hello.hello_spring.dto.adoption_request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionDto {
    @Schema(description = "질문 Type", example = "WHY_ADOPT")
    private String type;

    @Schema(description = "질문", example = "강아지를 입양하려는 이유는 무엇인가요?")
    private String question;
}
