package hello.hello_spring.controller;

import hello.hello_spring.domain.AdoptionRequest;
import hello.hello_spring.dto.adoption_request.AdoptionAnswerRequest;
import hello.hello_spring.service.AdoptionService;
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

    // ✅ 1. 입양 신청 시작 (petId 전달)
    @PostMapping("/start")
    public ResponseEntity<Long> startAdoption(
            HttpServletRequest request,
            @RequestParam Long petId
    ) {
        Long requestId = adoptionService.createAdoptionRequest(request, petId);
        return ResponseEntity.ok(requestId);
    }

    // ✅ 2. 집 사진 저장
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

    // ✅ 3. 질문 답변 저장
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

    // ✅ 4. 최종 제출
    @PostMapping("/{id}/submit")
    public ResponseEntity<Void> submitRequest(@PathVariable("id") Long requestId) {
        adoptionService.submitRequest(requestId);
        return ResponseEntity.ok().build();
    }
}
