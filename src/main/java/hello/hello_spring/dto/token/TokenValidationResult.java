package hello.hello_spring.dto.token;

import hello.hello_spring.domain.jwt.token.Token.Type;
import hello.hello_spring.domain.jwt.token.TokenStatus;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class TokenValidationResult {
    private TokenStatus tokenStatus;
    private String tokenId;
    private Type tokenType;
    private Claims claims;

    public String getEmail() {
        if (claims == null) {
            throw new IllegalStateException("Claim value is null.");
        }

        return claims.getSubject();
    }

        public boolean isValid() {
        return TokenStatus.TOKEN_VALID == this.tokenStatus;
    }

}
