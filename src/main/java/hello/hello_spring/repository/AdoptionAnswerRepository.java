package hello.hello_spring.repository;

import hello.hello_spring.domain.AdoptionAnswer;
import hello.hello_spring.domain.AdoptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionAnswerRepository extends JpaRepository<AdoptionAnswer, Long> {
    List<AdoptionAnswer> findByAdoptionRequest(AdoptionRequest request);
}
