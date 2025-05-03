package hello.hello_spring.dto.adoption_request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionRequestResponseDTO {
    private Long requestId;
    private String applicantName;
    private String applicantEmail;
    private List<AnswerDTO> answers;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDTO {
        private String question;
        private String answer;
    }
}