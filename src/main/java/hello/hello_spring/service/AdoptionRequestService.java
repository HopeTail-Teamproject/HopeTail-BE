package hello.hello_spring.service;

import hello.hello_spring.domain.AdoptionRequest;
import hello.hello_spring.domain.member.Member;
import hello.hello_spring.domain.petPost.PetPost;
import hello.hello_spring.domain.AdoptionQuestionType;
import hello.hello_spring.dto.adoption_request.AdoptionAnswerDTO;
import hello.hello_spring.dto.adoption_request.AdoptionRequestResponseDTO;
import hello.hello_spring.repository.PetPostRepository;
import hello.hello_spring.repository.AdoptionRequestRepository;
import hello.hello_spring.repository.AdoptionAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdoptionRequestService {

    private final PetPostRepository petRepository;
    private final AdoptionRequestRepository adoptionRequestRepository;
    private final AdoptionAnswerRepository adoptionAnswerRepository;

    @Transactional(readOnly = true)
    public List<AdoptionRequestResponseDTO> getRequestsByPetId(Long petId) {
        PetPost pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유기견이 존재하지 않습니다."));

        List<AdoptionRequest> requests = adoptionRequestRepository.findByPet(pet);

        return requests.stream().map(request -> {
            Member applicant = request.getMember();
            List<AdoptionRequestResponseDTO.AnswerDTO> answers = adoptionAnswerRepository.findByAdoptionRequest(request).stream()
                    .map(answer -> new AdoptionRequestResponseDTO.AnswerDTO(
                            answer.getQuestionType().getQuestion(), // 이미 문자열이면 그대로 사용
                            answer.getAnswer()))
                    .toList();


            return new AdoptionRequestResponseDTO(
                    request.getId(),
                    applicant.getUsername(),
                    applicant.getEmail(),
                    answers
            );
        }).toList();
    }
}
