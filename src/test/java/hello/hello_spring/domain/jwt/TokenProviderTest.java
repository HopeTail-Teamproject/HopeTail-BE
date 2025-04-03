package hello.hello_spring.domain.jwt;

import hello.hello_spring.domain.jwt.blacklist.AccessTokenBlackList;
import hello.hello_spring.domain.jwt.token.TokenProvider;
import hello.hello_spring.domain.member.Member;
import hello.hello_spring.dto.token.TokenDto;
import hello.hello_spring.dto.token.TokenValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class TokenProviderTest {
    private final String secrete = "dGhpcyBpcyBteSBoaWRkZW4gand0IHNlY3JldGUga2V5LCB3aGF0IGlzIHlvdXIgand0IHNlY3JldGUga2V5Pw==";
    private final Long TokenValidationInSeconds = 3L;
    private AccessTokenBlackList accessTokenBlackList;
    private final TokenProvider tokenProvider = new TokenProvider(secrete, TokenValidationInSeconds, accessTokenBlackList);
    Member member = getMember();


    @Test
    void createToken() {
        TokenDto tokenDto = tokenProvider.createToken(member);
        log.info("Token -> {}", tokenDto.getToken());

    }

    @Test
    void validateTokenValid() {
        TokenDto tokenDto = tokenProvider.createToken(member);
        TokenValidationResult tokenValidationResult = tokenProvider.validateToken(tokenDto.getToken());

        Assertions.assertThat(tokenValidationResult.isValid()).isTrue();
    }

    @Test
    void validateTokenNotValid() throws InterruptedException {
        TokenDto tokenDto = tokenProvider.createToken(member);
        Thread.sleep(4000);
        TokenValidationResult tokenValidationResult = tokenProvider.validateToken(tokenDto.getToken());

        Assertions.assertThat(tokenValidationResult.isValid()).isFalse();
    }

    private Member getMember() {
        return Member.builder()
                .email("jinwoo2202@naver.com")
                .password("1234")
                .username("pjw")
                .role(Member.Role.USER)
                .build();
    }
}