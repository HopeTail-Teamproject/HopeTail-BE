package hello.hello_spring.dto.pet;

import hello.hello_spring.domain.petPost.PetPost;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PetPostResponseDto {

    private Long id;
    private String photoUrl;
    private String name;
    private Integer age;
    private String species;
    private String address;
    private String description;
    private String email;
    private LocalDateTime createdAt;

    public PetPostResponseDto(PetPost petPost){
        this.id = petPost.getId();
        this.photoUrl = petPost.getPhotoUrl();
        this.name = petPost.getName();
        this.age = petPost.getAge();
        this.species = petPost.getSpecies();
        this.address = petPost.getAddress();
        this.description = petPost.getDescription();
        this.email = petPost.getEmail();
        this.createdAt = petPost.getCreatedAt();
    }

}
