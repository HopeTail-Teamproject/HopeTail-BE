package hello.hello_spring.service;

import hello.hello_spring.domain.*;
import hello.hello_spring.domain.member.Member;
import hello.hello_spring.dto.adoption_request.AdoptionAnswerRequest;
import hello.hello_spring.dto.adoption_request.AdoptionRequestDetailResponse;
import hello.hello_spring.repository.*;
import hello.hello_spring.domain.jwt.token.TokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdoptionService {

    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final AdoptionRequestRepository adoptionRequestRepository;
    private final AdoptionAnswerRepository adoptionAnswerRepository;
    private final HomeImageRepository homeImageRepository;
    private final TokenProvider tokenProvider;

    // ✅ 로그인 유저 이메일 추출 (ChatService 방식)
    private String extractEmail(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            Claims claims = tokenProvider.getClaims(token);
            return claims.getSubject(); // 이메일
        }
        throw new IllegalStateException("유효하지 않은 JWT 토큰입니다.");
    }

    // ✅ 1. 입양 신청 생성
    @Transactional
    public Long createAdoptionRequest(HttpServletRequest request, Long petId) {
        String email = extractEmail(request);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("로그인 유저를 찾을 수 없습니다."));

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유기견이 존재하지 않습니다."));

        AdoptionRequest adoptionRequest = AdoptionRequest.builder()
                .member(member)
                .pet(pet)
                .status(AdoptionRequest.Status.IN_PROGRESS)
                .build();

        adoptionRequestRepository.save(adoptionRequest);
        return adoptionRequest.getId();
    }

    // ✅ 2. 집 이미지 저장
    @Transactional
    public void saveHomeImages(AdoptionRequest request, List<String> imageUrls) {
        for (String url : imageUrls) {
            HomeImage image = HomeImage.builder()
                    .adoptionRequest(request)
                    .imageUrl(url)
                    .build();
            homeImageRepository.save(image);
        }
    }

    // ✅ 3. 질문 응답 저장
    @Transactional
    public void saveAnswers(AdoptionRequest request, List<AdoptionAnswerRequest> answers) {
        for (AdoptionAnswerRequest dto : answers) {
            AdoptionAnswer answer = AdoptionAnswer.builder()
                    .adoptionRequest(request)
                    .questionType(dto.getQuestionType())
                    .answer(dto.getAnswer())
                    .build();
            adoptionAnswerRepository.save(answer);
        }
    }

    // ✅ 4. 신청 최종 제출
    @Transactional
    public void submitRequest(Long requestId) {
        AdoptionRequest request = adoptionRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("해당 신청서를 찾을 수 없습니다."));

        request.setStatus(AdoptionRequest.Status.SUBMITTED);
        request.setSubmittedAt(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<AdoptionRequestDetailResponse> getRequestsForPet(Long petId, HttpServletRequest request) {
        String email = extractEmail(request);

        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유기견이 존재하지 않습니다."));

        // 등록자 본인인지 확인
        if (!pet.getMember().getEmail().equals(email)) {
            throw new SecurityException("본인이 등록한 유기견의 신청서만 조회할 수 있습니다.");
        }

        List<AdoptionRequest> requests = adoptionRequestRepository.findByPet(pet);

        return requests.stream().map(req -> {
            List<String> imageUrls = homeImageRepository.findByAdoptionRequest(req)
                    .stream().map(HomeImage::getImageUrl).toList();

            List<AdoptionRequestDetailResponse.AnswerItem> answers =
                    adoptionAnswerRepository.findByAdoptionRequest(req).stream()
                            .map(ans -> new AdoptionRequestDetailResponse.AnswerItem(
                                    ans.getQuestionType(), ans.getAnswer()))
                            .toList();

            return AdoptionRequestDetailResponse.builder()
                    .requestId(req.getId())
                    .applicantEmail(req.getMember().getEmail())
                    .submittedAt(req.getSubmittedAt())
                    .homeImages(imageUrls)
                    .answers(answers)
                    .build();

        }).toList();
    }
}
