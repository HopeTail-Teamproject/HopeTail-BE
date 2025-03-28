package hello.hello_spring.repository;

import hello.hello_spring.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //기본 틀

    //Spring Data JPA 때문에
    //save, findAll, deleteById 자동 생성
}
