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

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    // 전체 유기견 조회 (WAITING 상태)
    public List<PetResponseDto> getAllWaitingPets() {
        return petRepository.findByStatus(Pet.Status.WAITING)
                .stream()
                .map(pet -> new PetResponseDto(
                        pet.getId(),
                        pet.getName(),
                        pet.getAge(),
                        pet.getGender().name(),
                        pet.getBreed(),
                        pet.getDescription(),
                        pet.getImage(),
                        pet.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    // 단건 조회
    public PetResponseDto getPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        return new PetResponseDto(
                pet.getId(),
                pet.getName(),
                pet.getAge(),
                pet.getGender().name(),
                pet.getBreed(),
                pet.getDescription(),
                pet.getImage(),
                pet.getStatus().name()
        );
    }
}

