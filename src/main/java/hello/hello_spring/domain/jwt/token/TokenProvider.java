package hello.hello_spring.domain.jwt.token;


import hello.hello_spring.domain.member.Member;
import hello.hello_spring.domain.member.MemberPrinciple;
import hello.hello_spring.dto.token.TokenDto;
import hello.hello_spring.dto.token.TokenValidationResult;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

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

        // Refresh 토큰

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

    public TokenValidationResult validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(hashKey).build().parseClaimsJws(token).getBody();
            return new TokenValidationResult(TokenStatus.TOKEN_VALID,
                    claims.get(TOKEN_ID_KEY, String.class),
                    Token.Type.ACCESS,
                    claims);
        } catch (ExpiredJwtException e) {
            log.info("만료된 jwt 토큰");
            return getExpiredValidationToken(e);
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명");
            return new TokenValidationResult(TokenStatus.TOKEN_WRONG_SIGNATURE, null, null, null);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 서명");
            return new TokenValidationResult(TokenStatus.TOKEN_HASH_NOT_SUPPORTED, null, null, null);
        } catch (IllegalArgumentException e) {
            log.info("잘못된 JWT 토큰");
            return new TokenValidationResult(TokenStatus.TOKEN_WRONG_SIGNATURE, null, null, null);
        }

    }

    public Authentication getAuthentication(String token, Claims claims) {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        MemberPrinciple principle = new MemberPrinciple(claims.get(USERNAME_KEY, String.class), claims.getSubject(),
                authorities);

        return new UsernamePasswordAuthenticationToken(principle, token, authorities);

    }

    private TokenValidationResult getExpiredValidationToken(ExpiredJwtException e) {
        Claims claims = e.getClaims();
        return new TokenValidationResult(TokenStatus.TOKEN_EXPIRED,
                claims.get(TOKEN_ID_KEY, String.class),
                Token.Type.ACCESS,
                null); // null 변동가능 (향후 claims 활용 시) //
    }
}
