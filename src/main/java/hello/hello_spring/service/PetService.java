package hello.hello_spring.service;

import hello.hello_spring.domain.Pet;
import hello.hello_spring.domain.PetRegistration;
import hello.hello_spring.domain.member.Member;
import hello.hello_spring.dto.PetRegistrationRequestDto;
import hello.hello_spring.dto.PetRegistrationResponseDto;
import hello.hello_spring.dto.PetResponseDto;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.PetRegistrationRepository;
import hello.hello_spring.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import hello.hello_spring.dto.PetCreateRequestDto;
import hello.hello_spring.dto.PetResponseDto;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final MemberRepository memberRepository;

    public PetResponseDto createPet(PetCreateRequestDto dto) {
        Member member = memberRepository.findById(dto.getRegisteredById())
                .orElseThrow(() -> new RuntimeException("등록자(member) 찾을 수 없음"));

        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setAge(dto.getAge());
        pet.setGender(dto.getGender());
        pet.setSpecies(dto.getSpecies());
        pet.setNeutered(dto.isNeutered());
        pet.setDescription(dto.getDescription());
        pet.setTrained(dto.isTrained());
        pet.setVaccinated(dto.isVaccinated());
        pet.setImage(dto.getImage());
        pet.setRegisteredBy(member);

        Pet saved = petRepository.save(pet);
        return new PetResponseDto(saved);
    }

    public List<PetResponseDto> getAllPets() {

        return petRepository.findAll().stream()
                .map(PetResponseDto::new)
                .collect(Collectors.toList());
    }

    public PetResponseDto getPet(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 유기견을 찾을 수 없습니다."));
        return new PetResponseDto(pet);
    }
}

