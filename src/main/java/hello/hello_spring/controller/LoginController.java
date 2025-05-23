package hello.hello_spring.controller;

import hello.hello_spring.domain.jwt.token.RefreshToken;
import hello.hello_spring.domain.jwt.token.TokenProvider;
import hello.hello_spring.domain.member.Member;
import hello.hello_spring.domain.member.MemberPrinciple;
import hello.hello_spring.dto.login.LoginResponseDto;
import hello.hello_spring.dto.member.MemberCreateDto;
import hello.hello_spring.dto.member.MemberLoginDto;
import hello.hello_spring.dto.token.TokenDto;
import hello.hello_spring.service.LoginService;
import hello.hello_spring.web.json.ApiResponseJson;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "회원가입")
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
    @Operation(summary = "로그인")
    @PostMapping("/api/account/auth")
    public ApiResponseJson authenticateAccountAndIssueToken(@Valid @RequestBody MemberLoginDto memberLoginDto,
                                                            HttpServletResponse response,
                                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        LoginResponseDto loginResponseDto = loginService.loginMember(memberLoginDto.getEmail(),
                memberLoginDto.getPassword(),
                memberLoginDto.isRememberMe(),
                response);


        log.info("Token issued for account: {}", loginResponseDto.getAccessToken().getTokenId());

        return new ApiResponseJson(HttpStatus.OK, loginResponseDto);
    }

    @Operation(summary = "유저 정보 조회")
    @GetMapping("/api/account/userinfo")
    public ApiResponseJson getUserInfo(@AuthenticationPrincipal MemberPrinciple memberPrinciple) {
        log.info("요청 이메일 : {}", memberPrinciple.getEmail());

        Member userinfo = loginService.getUserInfo(memberPrinciple.getEmail());


        return new ApiResponseJson(HttpStatus.OK, userinfo);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/api/account/logout")
    public ApiResponseJson logoutUser(@AuthenticationPrincipal MemberPrinciple memberPrinciple,
                                      HttpServletResponse response) {
        log.info("로그아웃 요청 이메일 : {}", memberPrinciple.getEmail());
        RefreshToken refreshToken = loginService.findRefreshToken(memberPrinciple.getEmail());
        loginService.logout(memberPrinciple.getAccessToken(), memberPrinciple.getEmail(),
                refreshToken, response);

        return new ApiResponseJson(HttpStatus.OK, "OK. BYE~~");
    }

    // 토큰 재발급 api
    @Operation(summary = "토큰 재발급 요청")
    @PostMapping("/api/account/auth/refresh")
    public ApiResponseJson reissueToken(HttpServletRequest request) {
        String refreshToken = loginService.extractRefreshToken(request);
        String email = loginService.extractEmail(refreshToken);
        TokenDto tokenDto = loginService.getNewToken(email);
        return new ApiResponseJson(HttpStatus.OK, tokenDto);

    }

}
