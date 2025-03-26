package hello.hello_spring.domain.jwt.token;


import hello.hello_spring.domain.member.Member;
import hello.hello_spring.dto.TokenDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String USERNAME_KEY = "username";
    private static final String TOKEN_ID_KEY = "tokenId";
    private final Key hashKey;
    private final long TokenValidationInMilliseconds;

    public TokenProvider(String secrete, long TokenValidationInSeconds) {
        byte[] keyBytes = Decoders.BASE64.decode(secrete);
        this.hashKey = Keys.hmacShaKeyFor(keyBytes);
        this.TokenValidationInMilliseconds = TokenValidationInSeconds * 1000;
    }

    public TokenDto createToken(Member member) {
        long currentTime = (new Date()).getTime();

        Date TokenExpireTime = new Date(currentTime + this.TokenValidationInMilliseconds);
        String tokenId = UUID.randomUUID().toString();

        // Access 토큰
        String accessToken = Jwts.builder()
                .setSubject(member.getEmail())
                .claim(AUTHORITIES_KEY, member.getRole())
                .claim(USERNAME_KEY, member.getUsername())
                .claim(TOKEN_ID_KEY, tokenId)
                .signWith(hashKey, SignatureAlgorithm.HS512)
                .setExpiration(TokenExpireTime)
                .compact();

        // Certification 토큰

//        String certificationToken = Jwts.builder()
//                .setSubject(member.getEmail())
//                .claim(AUTHORITIES_KEY, member.getRole())
//                .claim(USERNAME_KEY, member.getUsername())
//                .claim(TOKEN_ID_KEY, tokenId)
//                .signWith(hashKey, SignatureAlgorithm.HS512)
//                .setExpiration(TokenExpireTime)
//                .compact();

        // PasswordReset 토큰

//        String passwordResetToken = Jwts.builder()
//                .setSubject(member.getEmail())
//                .claim(AUTHORITIES_KEY, member.getRole())
//                .claim(USERNAME_KEY, member.getUsername())
//                .claim(TOKEN_ID_KEY, tokenId)
//                .signWith(hashKey, SignatureAlgorithm.HS512)
//                .setExpiration(TokenExpireTime)
//                .compact();


        return TokenDto.builder()
                .email(member.getEmail())
                .Token(accessToken)
                .tokenId(tokenId)
                .tokenExpiredAt(TokenExpireTime)
                .build();

    }
}
