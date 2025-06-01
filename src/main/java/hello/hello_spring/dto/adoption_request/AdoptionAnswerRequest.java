package hello.hello_spring.dto.adoption_request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import hello.hello_spring.domain.AdoptionQuestionType;

@Getter
@Setter
public class AdoptionAnswerRequest {
    @Schema(description = "질문 Type", example = "WHY_ADOPT")
    private AdoptionQuestionType questionType;

    @Schema(description = "답변", example = "강아지를 사랑합니다.")
    private String answer;
}