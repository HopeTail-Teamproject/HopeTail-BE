package hello.hello_spring.dto;

import hello.hello_spring.domain.jwt.token.Token.Type;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.ToString;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@ToString(exclude = {"Token"})
public class TokenDto {
    private Long id;
    private String Token;
    private LocalDateTime tokenExpiredAt;
    private String email;
    private Type type;

    @Builder
    public TokenDto(Long id, String Token, LocalDateTime tokenExpiredAt, String email, Type type){
        this.id = id;
        this.Token = Token;
        this.tokenExpiredAt = tokenExpiredAt;
        this.email = email;
        this.type = type;
    }
}
