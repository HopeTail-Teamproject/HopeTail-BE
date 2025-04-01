package hello.hello_spring.controller;

import hello.hello_spring.domain.member.Member;
import hello.hello_spring.dto.member.MemberCreateDto;
import hello.hello_spring.service.LoginService;
import hello.hello_spring.web.json.ApiResponseJson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/api/account/create")
    public ApiResponseJson createNewAccount(@Valid @RequestBody MemberCreateDto memberCreateDto,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        Member member = loginService.createMember(memberCreateDto);

        log.info("Account successfully created with details: {}", member);

        return new ApiResponseJson(HttpStatus.OK, Map.of(
                "email", member.getEmail(),
                "username", member.getUsername()
        ));

    }

}
