package hello.hello_spring.repository;

<<<<<<< HEAD
import hello.hello_spring.domain.Member;
=======
import hello.hello_spring.domain.member.Member;
>>>>>>> member
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
