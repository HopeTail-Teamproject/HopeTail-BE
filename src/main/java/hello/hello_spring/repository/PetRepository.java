package hello.hello_spring.repository;

import hello.hello_spring.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByStatus(Pet.Status status);
}