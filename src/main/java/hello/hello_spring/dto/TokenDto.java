package hello.hello_spring.dto;

import hello.hello_spring.domain.jwt.token.Token.Type;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.ToString;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@ToString(exclude = {"Token"})
public class TokenDto {
    private String tokenId;
    private String Token;
    private Date tokenExpiredAt;
    private String email;

    @Builder
    public TokenDto(String tokenId, String Token, Date tokenExpiredAt, String email){
        this.tokenId = tokenId;
        this.Token = Token;
        this.tokenExpiredAt = tokenExpiredAt;
        this.email = email;
    }
}
