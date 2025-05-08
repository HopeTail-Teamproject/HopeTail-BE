package hello.hello_spring.controller;

import hello.hello_spring.domain.AdoptionRequest;
import hello.hello_spring.dto.adoption_request.AdoptionAnswerRequest;
import hello.hello_spring.service.AdoptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/adoption")
public class AdoptionController {

    private final AdoptionService adoptionService;

    @Operation(summary = "입양 신청 시작 (petId 전달)", description = "특정 반려견의 입양 신청을 시작합니다. petId를 전달하여 신청 절차를 시작합니다. 신청 ID를 응답 받습니다.")
    @PostMapping("/start")
    public ResponseEntity<Long> startAdoption(
            HttpServletRequest request,
            @RequestParam Long petId
    ) {
        Long requestId = adoptionService.createAdoptionRequest(request, petId);
        return ResponseEntity.ok(requestId);
    }

    @Operation(summary = "집 사진 저장", description = "입양 신청 과정에서 집 사진을 업로드합니다. 신청 id를 입력하고 이미지 주소를 입력합니다.")
    @PostMapping("/{id}/images")
    public ResponseEntity<Void> uploadImages(
            @PathVariable("id") Long requestId,
            @RequestBody List<String> imageUrls
    ) {
        AdoptionRequest request = new AdoptionRequest();
        request.setId(requestId); // 간단한 방식으로 참조
        adoptionService.saveHomeImages(request, imageUrls);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "질문 답변 저장", description = "입양 신청자가 질문에 대해 작성한 답변을 저장합니다. 신청 id를 입력하고 questionType에 대한 answer를 입력합니다.")
    @PostMapping("/{id}/answers")
    public ResponseEntity<Void> saveAnswers(
            @PathVariable("id") Long requestId,
            @RequestBody List<AdoptionAnswerRequest> answers
    ) {
        AdoptionRequest request = new AdoptionRequest();
        request.setId(requestId);
        adoptionService.saveAnswers(request, answers);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "입양 신청 최종 제출",
            description = "모든 정보를 입력한 후, 입양 신청을 최종 제출합니다. " +
                    "신청 ID를 전달하여 제출 상태로 전환합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "입양 신청이 성공적으로 제출됨"),
                    @ApiResponse(responseCode = "404", description = "해당 ID의 입양 신청이 존재하지 않음"),
                    @ApiResponse(responseCode = "500", description = "서버 오류로 인해 입양 신청 제출 실패")
            }
    )
    @PostMapping("/{id}/submit")
    public ResponseEntity<Void> submitRequest(@PathVariable("id") Long requestId) {
        adoptionService.submitRequest(requestId);
        return ResponseEntity.ok().build();
    }
}
