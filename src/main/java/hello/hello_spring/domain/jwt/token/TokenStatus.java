package hello.hello_spring.domain.jwt.token;

import lombok.Getter;

@Getter
public enum TokenStatus {
    TOKEN_VALID("유효한 토큰입니다."),
    TOKEN_EXPIRED("만료된 토큰입니다."),
    TOKEN_ABANDONED("버려진 토큰입니다."),

    TOKEN_WRONG_SIGNATURE("잘못된 토큰입니다."),
    TOKEN_HASH_NOT_SUPPORTED("지원하지 않는 형식의 토큰입니다."),
    WRONG_AUTH_HEADER("[Bearer ]로 시작하는 토큰이 없습니다."),
    TOKEN_VALIDATION_TRY_FAILED("인증에 실패했습니다.");


    private final String message;

    TokenStatus(String message) {
        this.message = message;
    }
}
