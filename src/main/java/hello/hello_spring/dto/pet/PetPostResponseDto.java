package hello.hello_spring.dto.pet;

import hello.hello_spring.domain.petPost.PetPost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PetPostResponseDto {

    @Schema(description = "게시물 ID", example = "1")
    private Long id;

    @Schema(description = "강아지 사진 URL", example = "https://example.com/images/dog.jpg")
    private String photoUrl;

    @Schema(description = "강아지 이름", example = "골든리트리버 골드")
    private String name;

    @Schema(description = "강아지 나이(살)", example = "3")
    private Integer age;

    @Schema(description = "강아지 품종", example = "Golden Retriever")
    private String species;

    @Schema(description = "현재 위치(주소)", example = "서울특별시 강남구 테헤란로 123")
    private String address;

    @Schema(description = "추가 설명", example = "임시보호 중인 귀여운 골든리트리버입니다.")
    private String description;

    @Schema(description = "등록자 이메일", example = "user@example.com")
    private String email;

    @Schema(description = "게시물 생성일시", example = "2025-05-01T14:30:00")
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
