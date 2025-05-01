package hello.hello_spring.repository;

import hello.hello_spring.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    PostLike findByPostIdAndEmail(Long postId, String email);
}
