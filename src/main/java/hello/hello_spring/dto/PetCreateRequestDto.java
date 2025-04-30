package hello.hello_spring.dto;

import hello.hello_spring.domain.Pet;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetCreateRequestDto {
    private String name;
    private Integer age;
    private Pet.Gender gender;
    private String breed;
    private String description;
    private String image;
}

