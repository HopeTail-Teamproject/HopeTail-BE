package hello.hello_spring.dto.adoption_request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdoptionAnswerDTO {
    private String question;
    private String answer;
}
