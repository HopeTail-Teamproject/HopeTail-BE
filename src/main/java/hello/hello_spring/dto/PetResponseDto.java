package hello.hello_spring.dto;

import hello.hello_spring.domain.Pet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PetResponseDto {

    private Long id;
    private String name;
    private int age;
    private String gender;
    private String breed;
    private String description;
    private String image;
    private String status;

    public PetResponseDto(Pet pet) {
        this.id = pet.getId();
        this.name = pet.getName();
        this.age = pet.getAge();
        this.gender = pet.getGender().name();
        this.breed = pet.getBreed();
        this.description = pet.getDescription();
        this.image = pet.getImage();
        this.status = pet.getStatus().name();
    }
}
