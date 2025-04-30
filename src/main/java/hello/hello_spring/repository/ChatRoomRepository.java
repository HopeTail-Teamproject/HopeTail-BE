package hello.hello_spring.repository;

import hello.hello_spring.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    /**
     * member1.email 과 member2.email 조합이 (e1, e2) 또는 (e2, e1) 인
     * 동일한 1:1 채팅방을 찾는다.
     */
    @Query("""
        select c
        from ChatRoom c
        where (c.member1.email = :e1 and c.member2.email = :e2)
           or (c.member1.email = :e2 and c.member2.email = :e1)
    """)
    Optional<ChatRoom> findByEmails(String e1, String e2);
}
