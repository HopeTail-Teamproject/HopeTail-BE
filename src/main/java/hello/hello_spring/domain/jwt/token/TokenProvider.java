package hello.hello_spring.domain.jwt.token;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenProvider {
    public TokenProvider(String secrete, long accessTokenValidationInSeconds) {
//        byte[] keyBytes = Decoders.BASE64.decode(secrete);
    }
    public Token createToken() {
        return null;        //미완
    }
}
