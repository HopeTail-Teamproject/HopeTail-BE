package hello.hello_spring.dto;

import lombok.*;

// PetResponseDto.java
@Getter @Setter
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
}

