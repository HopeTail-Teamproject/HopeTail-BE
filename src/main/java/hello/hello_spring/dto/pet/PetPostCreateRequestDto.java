package hello.hello_spring.dto.pet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetPostCreateRequestDto {


    @Schema(description = "강아지 이름", example = "골드")
    private String name;

    @Schema(description = "강아지 나이(살)", example = "3")
    private Integer age;

    @Schema(description = "강아지 품종", example = "Golden Retriever")
    private String species;

    @Schema(description = "현재 위치(주소)", example = "서울특별시 강남구 테헤란로 123")
    private String address;

    @Schema(description = "추가 설명", example = "임시보호 중인 귀여운 골든리트리버입니다.")
    private String description;

}
