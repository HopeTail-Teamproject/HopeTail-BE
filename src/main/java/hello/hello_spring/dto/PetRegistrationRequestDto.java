package hello.hello_spring.dto;

import hello.hello_spring.domain.PetRegistration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

// PetRegistrationRequestDto.java
@Getter
@Setter
public class PetRegistrationRequestDto {
    private Long memberId;
    private String petInfo;
}

