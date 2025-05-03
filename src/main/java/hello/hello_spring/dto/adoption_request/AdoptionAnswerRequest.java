package hello.hello_spring.dto.adoption_request;

import lombok.Getter;
import lombok.Setter;
import hello.hello_spring.domain.AdoptionQuestionType;

@Getter
@Setter
public class AdoptionAnswerRequest {
    private AdoptionQuestionType questionType;
    private String answer;
}