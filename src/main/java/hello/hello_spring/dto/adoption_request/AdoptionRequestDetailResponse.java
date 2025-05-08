package hello.hello_spring.dto.adoption_request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AdoptionRequestDetailResponse {
    @Schema(description = "요청 ID", example = "Start 시 응답받는 ID")
    private Long requestId;

    @Schema(description = "입양 신청자의 이메일 주소", example = "hopetail@hopetail.com")
    private String applicantEmail;

    @Schema(description = "입양 요청 제출 시간 (ISO 형식)", example = "2025-05-08T12:34:56")
    private LocalDateTime submittedAt;

    @Schema(description = "신청자가 제출한 집 내부 사진 목록 (URL 형태)", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]")
    private List<String> homeImages;

    @Schema(description = "입양 신청에 대한 답변 목록", example = "[{\"questionType\": \"WHY_ADOPT\", \"answer\": \"강아지를 사랑합니다.\"}]")
    private List<AnswerItem> answers;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class AnswerItem {
        @Schema(description = "질문 Type", example = "WHY_ADOPT")
        private String questionType;

        @Schema(description = "답변", example = "강아지를 사랑합니다.")
        private String answer;
    }
}
