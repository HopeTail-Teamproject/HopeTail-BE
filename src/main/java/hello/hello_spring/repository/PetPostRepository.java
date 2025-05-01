package hello.hello_spring.repository;

import hello.hello_spring.domain.petPost.PetPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetPostRepository extends JpaRepository<PetPost, Long> {

}
