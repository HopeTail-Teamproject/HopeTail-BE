package hello.hello_spring.repository;

import hello.hello_spring.domain.AdoptionRequest;
import hello.hello_spring.domain.HomeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeImageRepository extends JpaRepository<HomeImage, Long> {
    List<HomeImage> findByAdoptionRequest(AdoptionRequest request);
    long countByAdoptionRequest(AdoptionRequest request);
}
