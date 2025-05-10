package hello.hello_spring.repository;

import hello.hello_spring.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface PostRepository extends JpaRepository<Post, Long> {

    //카테고리로 조회
    List<Post> findByCategory(Post.Category category);

    List<Post> findByEmail(String email);

    //Spring Data JPA 때문에
    //save, findAll, deleteById 자동 생성
}
