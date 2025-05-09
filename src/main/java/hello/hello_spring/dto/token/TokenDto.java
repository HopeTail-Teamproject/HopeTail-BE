package hello.hello_spring.dto.token;

import hello.hello_spring.domain.jwt.token.TokenType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString(exclude = {"Token"})
public class TokenDto {
    private String tokenId;
    private String Token;
    private Date tokenExpiredAt;
    private String email;

    @Builder
    public TokenDto(String tokenId, String Token, Date tokenExpiredAt, String email, TokenType tokenType) {
        this.tokenId = tokenId;
        this.Token = Token;
        this.tokenExpiredAt = tokenExpiredAt;
        this.email = email;
    }
}
