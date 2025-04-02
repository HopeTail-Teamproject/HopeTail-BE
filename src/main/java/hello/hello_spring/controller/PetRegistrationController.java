package hello.hello_spring.controller;

import hello.hello_spring.dto.CommentCreateRequestDto;
import hello.hello_spring.dto.CommentResponseDto;
import hello.hello_spring.dto.PetRegistrationRequestDto;
import hello.hello_spring.dto.PetRegistrationResponseDto;
import hello.hello_spring.service.CommentService;
import hello.hello_spring.service.PetRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pet-registrations")
@RequiredArgsConstructor
public class PetRegistrationController {

    private final PetRegistrationService registrationService;

    @PostMapping
    @Operation(summary = "유기견 등록 신청")
    public PetRegistrationResponseDto register(@RequestBody PetRegistrationRequestDto dto) {
        return registrationService.register(dto);
    }

    @GetMapping
    @Operation(summary = "모든 유기견 등록 신청 목록")
    public List<PetRegistrationResponseDto> getAll() {
        return registrationService.getAll();
    }
}
