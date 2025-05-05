package hello.hello_spring.repository;

import hello.hello_spring.domain.petPost.PetPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetPostLikeRepository extends JpaRepository<PetPostLike, Long> {
    PetPostLike findByPetPostIdAndEmail(Long petId, String email);
}
