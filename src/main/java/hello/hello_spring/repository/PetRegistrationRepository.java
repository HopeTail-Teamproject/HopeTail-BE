package hello.hello_spring.repository;

import hello.hello_spring.domain.PetRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetRegistrationRepository extends JpaRepository<PetRegistration, Long> {
    List<PetRegistration> findByStatus(PetRegistration.Status status);
}