package hello.hello_spring.controller;

import hello.hello_spring.dto.pet.PetPostCreateRequestDto;
import hello.hello_spring.dto.pet.PetPostResponseDto;
import hello.hello_spring.service.PetPostService;
import hello.hello_spring.web.json.ApiResponseJson;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/petposts")
@RequiredArgsConstructor
public class PetPostController {

    private final PetPostService petPostService;

    @Operation(summary = "유기견 게시")
    @PostMapping
    public PetPostResponseDto createPet(@RequestBody PetPostCreateRequestDto dto, HttpServletRequest request){
        return petPostService.createPet(dto, request);
    }

    @Operation(summary = "유기견 전체 조회")
    @GetMapping
    public List<PetPostResponseDto> getAllPets(){
        return petPostService.getAllPets();
    }

    @Operation(summary = "유기견 단건 조회")
    @GetMapping("/{petId}")
    public PetPostResponseDto getPet(@PathVariable Long petId){
        return petPostService.getPet(petId);
    }

    //수정은 일단 안해놓음
//    @Operation(summary = "유기견 글 수정")
//    @PutMapping("/{petId}")
//    public PetPostResponseDto updatePet(@PathVariable Long petId, @RequestBody PetPostUpdateRequestDto dto, HttpServletRequest request){
//        return petPostService.updatePet(petId, dto, request);
//    }

    @Operation(summary = "유기견 글 삭제")
    @DeleteMapping("/{petId}")
    public void deletePet(@PathVariable Long petId, HttpServletRequest request){
        petPostService.deletePet(petId, request);
    }

    @Operation(summary = "펫 좋아요")
    @PostMapping("/{petId}/like")
    public ApiResponseJson likePet(@PathVariable Long petId, HttpServletRequest request){
        petPostService.handlePetPostLikeButton(petId, request);
        return new ApiResponseJson(HttpStatus.OK, null);
    }
}
