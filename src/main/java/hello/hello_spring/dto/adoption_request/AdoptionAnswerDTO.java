package hello.hello_spring.dto.adoption_request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdoptionAnswerDTO {
    @Schema(description = "질문", example = "강아지를 입양하려는 이유는 무엇인가요?")
    private String question;

    @Schema(description = "답변", example = "강아지를 사랑합니다.")
    private String answer;
}
