package hello.hello_spring.dto.adoption_request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionDto {
    private String type;
    private String question;
}
