package hello.hello_spring.dto;

import hello.hello_spring.domain.Pet;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetCreateRequestDto {

    private String name;
    private int age;
    private Pet.Gender gender;
    private String breed;
    private String description;
    private String image;

    // 로그인 기반 인증 붙이면 memberId는 제거하고 토큰에서 주입 가능
    private Long memberId;
}
