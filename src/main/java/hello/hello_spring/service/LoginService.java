package hello.hello_spring.service;

import hello.hello_spring.domain.member.Member;
import hello.hello_spring.dto.member.MemberCreateDto;
import hello.hello_spring.repository.MemberRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&])[A-Za-z\\d$@!%*#?&]{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member createMember(MemberCreateDto memberCreateDto) {
        checkPasswordLength(memberCreateDto.getPassword());
        
        if (memberRepository.existsByEmail(memberCreateDto.getEmail())) {
            log.info("이미 등록된 이메일 {}", memberCreateDto.getEmail());
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        Member member = Member.builder().
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

}