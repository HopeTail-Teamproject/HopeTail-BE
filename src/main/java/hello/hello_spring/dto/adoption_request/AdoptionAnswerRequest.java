package hello.hello_spring.dto.adoption_request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdoptionAnswerRequest {
    private String questionType;
    private String answer;
}