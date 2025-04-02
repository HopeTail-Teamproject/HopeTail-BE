// PetRepository.java
package hello.hello_spring.repository;

import hello.hello_spring.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
    // 필요한 경우 상태별 검색 등도 추가 가능
}