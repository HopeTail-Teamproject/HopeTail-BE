package hello.hello_spring.dto.adoption_request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionRequestResponseDTO {
    @Schema(description = "요청 ID", example = "Start 시 응답받는 ID")
    private Long requestId;

    @Schema(description = "입양 신청자의 이름", example = "이민성")
    private String applicantName;

    @Schema(description = "입양 신청자의 이메일 주소", example = "hopetail@hopetail.com")
    private String applicantEmail;

    @Schema(description = "입양 신청에 대한 답변 목록", example = "[{\\\"questionType\\\": \\\"WHY_ADOPT\\\", \\\"answer\\\": \\\"강아지를 사랑합니다.\\\"}]")
    private List<AnswerDTO> answers;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDTO {
        @Schema(description = "질문", example = "강아지를 입양하려는 이유는 무엇인가요?")
        private String question;

        @Schema(description = "답변", example = "강아지를 사랑합니다.")
        private String answer;
    }
}