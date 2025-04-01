package hello.hello_spring.web.advice;

import hello.hello_spring.web.json.ApiResponseJson;
import hello.hello_spring.web.json.ResponseStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ApiResponseJson handleRuntimeException(RuntimeException e) {
        log.error("", e);
        return new ApiResponseJson(HttpStatus.INTERNAL_SERVER_ERROR, ResponseStatusCode.SERVER_ERROR,
                Map.of("errMsg", "서버에 오류가 발생했습니다."));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiResponseJson handleBadRequestException(BadRequestException e) {
        return new ApiResponseJson(HttpStatus.BAD_REQUEST, ResponseStatusCode.WRONG_PARAMETER,
                Map.of("errMsg", e.getMessage()));

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponseJson handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("", e);
        return new ApiResponseJson(HttpStatus.BAD_REQUEST, ResponseStatusCode.WRONG_PARAMETER,
                Map.of("errMsg", "비밀번호는 최소 8자리에 영어, 숫자, 특수문자를 포함해야 합니다."));
    }
}
