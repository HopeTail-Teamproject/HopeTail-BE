package hello.hello_spring.service;

import hello.hello_spring.domain.*;
import hello.hello_spring.domain.member.Member;
import hello.hello_spring.domain.petPost.PetPost;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdoptionService {

    private final MemberRepository memberRepository;
    private final PetPostRepository petRepository;
    private final AdoptionRequestRepository adoptionRequestRepository;
    private final AdoptionAnswerRepository adoptionAnswerRepository;
    private final HomeImageRepository homeImageRepository;
    private final TokenProvider tokenProvider;
    private final ImageService imageService;

    private String extractEmail(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            Claims claims = tokenProvider.getClaims(token);
            return claims.getSubject();
        }
        throw new IllegalStateException("유효하지 않은 JWT 토큰입니다.");
    }

    @Transactional
    public Long createAdoptionRequest(HttpServletRequest request, Long petId) {
        String email = extractEmail(request);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("로그인 유저를 찾을 수 없습니다."));
        PetPost pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유기견이 존재하지 않습니다."));

        AdoptionRequest adoptionRequest = AdoptionRequest.builder()
                .member(member)
                .pet(pet)
                .status(AdoptionRequest.Status.IN_PROGRESS)
                .build();

        adoptionRequestRepository.save(adoptionRequest);
        return adoptionRequest.getId();
    }

    @Transactional
    public void saveHomeImages(AdoptionRequest request, List<String> imageUrls) {
        for (String url : imageUrls) {
            homeImageRepository.save(
                    HomeImage.builder()
                            .adoptionRequest(request)
                            .imageUrl(url)
                            .build()
            );
        }
    }

    @Transactional
    public void saveAnswers(AdoptionRequest request, List<AdoptionAnswerRequest> answers) {
        for (AdoptionAnswerRequest dto : answers) {
            adoptionAnswerRepository.save(
                    AdoptionAnswer.builder()
                            .adoptionRequest(request)
                            .questionType(dto.getQuestionType())
                            .answer(dto.getAnswer())
                            .build()
            );
        }
    }

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

        PetPost pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유기견이 존재하지 않습니다."));

        if (!pet.getEmail().equals(email)) {
            throw new SecurityException("본인이 등록한 유기견의 신청서만 조회할 수 있습니다.");
        }

        List<AdoptionRequest> requests = adoptionRequestRepository.findByPet(pet);

        return requests.stream().map(req -> {
            List<String> imageUrls = homeImageRepository.findByAdoptionRequest(req).stream()
                    .map(HomeImage::getImageUrl).toList();

            List<AdoptionRequestDetailResponse.AnswerItem> answers =
                    adoptionAnswerRepository.findByAdoptionRequest(req).stream()
                            .map(ans -> new AdoptionRequestDetailResponse.AnswerItem(
                                    ans.getQuestionType().getQuestion(),
                                    ans.getAnswer()))
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

    @Transactional
    public void saveHomeImages(Long requestId, List<MultipartFile> imageFiles) {
        AdoptionRequest request = adoptionRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("입양 신청이 존재하지 않습니다."));

        for (MultipartFile image : imageFiles) {
            try {
                String imageUrl = imageService.saveImageGetUrl(image);
                HomeImage homeImage = HomeImage.builder()
                        .adoptionRequest(request)
                        .imageUrl(imageUrl)
                        .build();
                homeImageRepository.save(homeImage);
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 중 오류 발생", e);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<String> getHomeImageUrls(Long requestId) {
        AdoptionRequest request = adoptionRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("입양 신청이 존재하지 않습니다."));

        return homeImageRepository.findByAdoptionRequest(request).stream()
                .map(HomeImage::getImageUrl)
                .toList();
    }
}