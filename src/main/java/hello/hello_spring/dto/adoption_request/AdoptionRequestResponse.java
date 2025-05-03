package hello.hello_spring.dto.adoption_request;

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
public class AdoptionRequestResponse {

    private Long requestId;

    private String applicantEmail;

    private LocalDateTime submittedAt;

    private List<String> imageUrls;

    private List<AnswerItem> answers;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class AnswerItem {
        private String question;
        private String answer;
    }
}