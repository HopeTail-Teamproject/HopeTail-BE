package hello.hello_spring.dto;

import hello.hello_spring.domain.Pet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetCreateRequestDto {
    @Schema(description = "유기견 이름")
    private String name;

    @Schema(description = "유기견 나이")
    private int age;

    @Schema(description = "유기견 성별 (MALE / FEMALE)")
    private Pet.Gender gender;

    @Schema(description = "유기견 종")
    private String species;

    @Schema(description = "중성화 여부")
    private boolean neutered;

    @Schema(description = "성격 및 건강상태")
    private String description;

    @Schema(description = "배변 교육 여부")
    private boolean trained;

    @Schema(description = "예방접종 여부")
    private boolean vaccinated;

    @Schema(description = "구조 및 보호 히스토리")
    private String history;

    @Schema(description = "이미지 경로")
    private String image;

    @Schema(description = "등록한 회원 ID")
    private Long registeredById;
}