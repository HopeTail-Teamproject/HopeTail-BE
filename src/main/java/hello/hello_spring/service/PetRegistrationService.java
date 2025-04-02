package hello.hello_spring.service;

import hello.hello_spring.domain.PetRegistration;
import hello.hello_spring.domain.member.Member;
import hello.hello_spring.dto.PetRegistrationRequestDto;
import hello.hello_spring.dto.PetRegistrationResponseDto;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.PetRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// PetRegistrationService.java
@Service
@RequiredArgsConstructor
public class PetRegistrationService {

    private final PetRegistrationRepository registrationRepository;
    private final MemberRepository memberRepository;

    public PetRegistrationResponseDto register(PetRegistrationRequestDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        PetRegistration registration = new PetRegistration();
        registration.setMember(member);
        registration.setPetInfo(dto.getPetInfo());
        registration.setStatus(PetRegistration.Status.PENDING);

        PetRegistration saved = registrationRepository.save(registration);

        return new PetRegistrationResponseDto(
                saved.getId(),
                saved.getPetInfo(),
                saved.getStatus().name(),
                saved.getMember().getId()
        );
    }

    public List<PetRegistrationResponseDto> getAll() {
        return registrationRepository.findAll().stream()
                .map(reg -> new PetRegistrationResponseDto(
                        reg.getId(),
                        reg.getPetInfo(),
                        reg.getStatus().name(),
                        reg.getMember().getId()
                )).collect(Collectors.toList());
    }
}

