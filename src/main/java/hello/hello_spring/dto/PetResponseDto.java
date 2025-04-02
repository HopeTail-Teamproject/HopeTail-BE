package hello.hello_spring.dto;

import hello.hello_spring.domain.Pet;
import lombok.*;

// PetResponseDto.java
@Getter @Setter
@AllArgsConstructor
public class PetResponseDto {
    private Long id;
    private String name;
    private int age;
    private String gender;
    private String species;
    private boolean neutered;
    private String description;
    private boolean trained;
    private boolean vaccinated;
    private String image;
    private String status;

    public PetResponseDto(Pet pet) {
        this.id = pet.getId();
        this.name = pet.getName();
        this.age = pet.getAge();
        this.gender = pet.getGender().name();
        this.species = pet.getSpecies();
        this.neutered = pet.isNeutered();
        this.description = pet.getDescription();
        this.trained = pet.isTrained();
        this.vaccinated = pet.isVaccinated();
        this.image = pet.getImage();
        this.status = pet.getStatus().name();
    }
}

