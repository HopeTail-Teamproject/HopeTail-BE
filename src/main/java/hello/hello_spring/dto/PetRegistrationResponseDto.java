package hello.hello_spring.dto;

import lombok.*;

// PetRegistrationResponseDto.java
@Getter
@Setter
@AllArgsConstructor
public class PetRegistrationResponseDto {
    private Long id;
    private String petInfo;
    private String status;
    private Long memberId;
}