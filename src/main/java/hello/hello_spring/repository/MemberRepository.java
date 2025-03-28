package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일로 멤버 조회
    Optional<Member> findByEmail(String email);

    // 사용자 이름으로 멤버 조회
    Optional<Member> findByUsername(String username);
}
