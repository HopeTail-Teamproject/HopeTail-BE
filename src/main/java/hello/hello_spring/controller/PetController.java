package hello.hello_spring.controller;

import hello.hello_spring.domain.member.Member;
import hello.hello_spring.dto.pet.PetCreateRequestDto;
import hello.hello_spring.dto.pet.PetResponseDto;
import hello.hello_spring.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
            @RequestBody PetCreateRequestDto dto,
            @AuthenticationPrincipal Member member
    ) {
        //로그인 인증 이후에
        //return petService.createPet(dto, member);
        return petService.createPet(dto);
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
