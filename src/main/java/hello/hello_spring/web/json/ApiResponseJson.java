package hello.hello_spring.web.json;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString

public class ApiResponseJson {
    public HttpStatus httpStatus;
    public int code;
    public Object data;

    public ApiResponseJson(HttpStatus httpStatus, Object data) {
        this.httpStatus = httpStatus;
        this.code = ResponseStatusCode.OK;
        this.data = data;
    }

    public ApiResponseJson(HttpStatus httpStatus, int code, Object data) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.data = data;
    }
}
