package hello.hello_spring.repository;

import hello.hello_spring.domain.AdoptionRequest;
import hello.hello_spring.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, Long> {
    List<AdoptionRequest> findByPet(Pet pet);
}
