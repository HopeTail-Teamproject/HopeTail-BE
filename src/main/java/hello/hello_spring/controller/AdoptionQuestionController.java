package hello.hello_spring.controller;

import hello.hello_spring.domain.AdoptionQuestionType;
import hello.hello_spring.dto.adoption_request.QuestionDto;
import hello.hello_spring.service.AdoptionQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/adoption/questions")
public class AdoptionQuestionController {

    private final AdoptionQuestionService questionService;

    @GetMapping
    public List<QuestionDto> getQuestions() {
        return Arrays.stream(AdoptionQuestionType.values())
                .map(q -> new QuestionDto(q.name(), q.getQuestion()))
                .collect(Collectors.toList());
    }
}
