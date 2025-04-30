package hello.hello_spring.controller;

import hello.hello_spring.domain.member.Member;
import hello.hello_spring.dto.pet.PetCreateRequestDto;
import hello.hello_spring.dto.pet.PetResponseDto;
import hello.hello_spring.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    @Operation(summary = "유기견 등록")
    public PetResponseDto createPet(
            @Valid @RequestBody PetCreateRequestDto dto,
            @AuthenticationPrincipal Member member
    ) {
        System.out.println("=== 요청 받은 PetCreateRequestDto ===");
        System.out.println("이름: " + dto.getName());
        System.out.println("나이: " + dto.getAge());
        System.out.println("성별: " + dto.getGender());
        System.out.println("품종: " + dto.getBreed());
        System.out.println("설명: " + dto.getDescription());
        System.out.println("이미지: " + dto.getImage());
        System.out.println("=========================");

        return petService.createPet(dto, member);
    }


    @GetMapping
    @Operation(summary = "전체 유기견 조회")
    public List<PetResponseDto> getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("/{id}")
    @Operation(summary = "단일 유기견 조회")
    public PetResponseDto getPet(@PathVariable Long id) {
        return petService.getPet(id);
    }
}
