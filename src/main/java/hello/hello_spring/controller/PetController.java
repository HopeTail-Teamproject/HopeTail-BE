package hello.hello_spring.controller;

import hello.hello_spring.dto.PetCreateRequestDto;
import hello.hello_spring.dto.PetResponseDto;
import hello.hello_spring.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    @Operation(summary = "유기견 등록 (관리자 승인 시 호출)")
    public PetResponseDto createPet(@RequestBody PetCreateRequestDto dto) {
        return petService.createPet(dto);
    }

    @GetMapping
    @Operation(summary = "전체 유기견 목록 조회")
    public List<PetResponseDto> getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("/{id}")
    @Operation(summary = "유기견 단건 조회")
    public PetResponseDto getPet(@PathVariable Long id) {
        return petService.getPet(id);
    }
}