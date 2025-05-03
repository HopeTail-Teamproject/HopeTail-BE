package hello.hello_spring.controller;

import hello.hello_spring.dto.adoption_request.AdoptionRequestDetailResponse;
import hello.hello_spring.service.AdoptionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/adoption")
public class AdoptionAdminController {

    private final AdoptionService adoptionService;

    // ✅ 유기견 신청서 목록 조회
    @GetMapping("/{petId}/requests")
    public ResponseEntity<List<AdoptionRequestDetailResponse>> getRequests(
            @PathVariable Long petId,
            HttpServletRequest request
    ) {
        List<AdoptionRequestDetailResponse> result = adoptionService.getRequestsForPet(petId, request);
        return ResponseEntity.ok(result);
    }
}
