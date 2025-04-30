package hello.hello_spring.service;

import hello.hello_spring.InvalidRefreshTokenException;
import hello.hello_spring.domain.jwt.blacklist.AccessTokenBlackList;
import hello.hello_spring.domain.jwt.token.RefreshToken;
import hello.hello_spring.domain.jwt.token.TokenProvider;
import hello.hello_spring.domain.member.Member;
import hello.hello_spring.dto.login.LoginResponseDto;
import hello.hello_spring.dto.member.MemberCreateDto;
import hello.hello_spring.dto.token.TokenDto;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&])[A-Za-z\\d$@!%*#?&]{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AccessTokenBlackList accessTokenBlackList;

    public Member createMember(MemberCreateDto memberCreateDto) {
        checkPasswordLength(memberCreateDto.getPassword());
        
        if (memberRepository.existsByEmail(memberCreateDto.getEmail())) {
            log.info("이미 등록된 이메일 {}", memberCreateDto.getEmail());
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        Member member = Member.builder().
                address(memberCreateDto.getAddress()).
                phoneNumber(memberCreateDto.getPhoneNumber()).
                email(memberCreateDto.getEmail()).
                password(passwordEncoder.encode(memberCreateDto.getPassword())).
                username(memberCreateDto.getUsername()).
                role(Member.Role.USER).
                build();

        return memberRepository.save(member);
    }

    private void checkPasswordLength(@NotNull String password) {
        if (PASSWORD_PATTERN.matcher(password).matches()) {
            return;
        }

        log.info("비밀번호 정책 미달");
        throw new IllegalArgumentException("비밀번호는 최소 8자리에 영어, 숫자, 특수문자를 포함해야 합니다.");
    }

    public LoginResponseDto loginMember(String email, String password,
                                boolean rememberMe, HttpServletResponse response) {
        try {
            Member member = findMemberByEmail(email);

            checkPassword(password, member);

            member.setLastLogin(LocalDateTime.now());
            memberRepository.save(member);

            TokenDto accessTokenDto = tokenProvider.createAccessToken(member);
            TokenDto refreshTokenDto = tokenProvider.createRefreshToken(member);

            RefreshToken refreshToken = RefreshToken.builder()
                    .tokenId(refreshTokenDto.getTokenId())
                    .Token(refreshTokenDto.getToken())
                    .member(member)
                    .email(email)
                    .build();

            refreshTokenRepository.save(refreshToken);

            if (rememberMe) {
                ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshTokenDto.getToken())
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .sameSite("Strict")
                        .maxAge(Duration.ofDays(10))
                        .build();

                response.setHeader("Set-Cookie", cookie.toString());

                return new LoginResponseDto(accessTokenDto, null);
            }

            else {
                return new LoginResponseDto(accessTokenDto, refreshTokenDto);
            }

        } catch (IllegalArgumentException | BadCredentialsException e) {
            throw new IllegalArgumentException("계정이 존재하지 않거나 비밀번호가 잘못되었습니다.");
        }

    }

    private void checkPassword(String password, Member member) {
        if (!passwordEncoder.matches(password, member.getPassword())) {
            log.info("일치하지 않는 비밀번호");
            throw new BadCredentialsException("기존 비밀번호 확인에 실패했습니다.");
        }
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> {
            log.info("계정이 존재하지 않음");
            return new IllegalArgumentException("계정이 존재하지 않습니다.");
        });
    }

    public Member getUserInfo(String email) {

        Member member = findMemberByEmail(email);

        Member userinfo = Member.builder()
                .address(member.getAddress())
                .email(member.getEmail())
                .username(member.getUsername())
                .role(member.getRole())
                .phoneNumber(String.valueOf(member.getPhoneNumber()))
                .build();

        return userinfo;
    }

    public void logout(String accessToken, String email, RefreshToken refreshToken, HttpServletResponse response) {
        accessTokenBlackList.setBlackList(accessToken, email);
        if (refreshToken != null) {
            refreshTokenRepository.delete(refreshToken);
        }

        else {
            ResponseCookie cookie = ResponseCookie.from("refreshToken", "null")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(Duration.ofDays(0))
                    .build();

            response.setHeader("Set-Cookie", cookie.toString());
        }

    }

    public TokenDto getNewToken(String email) {
        Member member = findMemberByEmail(email);
        return tokenProvider.createAccessToken(member);
    }

    public String extractRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public String extractEmail(String refreshToken) {
        Claims claims = tokenProvider.getClaims(refreshToken);
        String email = claims.getSubject();

        RefreshToken storedToken = refreshTokenRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("해당되는 토큰이 없습니다."));

        if (!refreshToken.equals(storedToken.getToken())) {
            throw new InvalidRefreshTokenException("토큰이 일치하지 않습니다.");
        }

        return email;
    }

    public RefreshToken findRefreshToken(String email) {
        return refreshTokenRepository.findByEmail(email).orElse(null);
    }
}