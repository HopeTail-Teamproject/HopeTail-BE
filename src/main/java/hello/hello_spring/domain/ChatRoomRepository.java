package hello.hello_spring.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 1:1 채팅에서 member1, member2가 동일한 채팅방을 찾기 위한 메서드
    Optional<ChatRoom> findByMember1AndMember2(Member member1, Member member2);
}
