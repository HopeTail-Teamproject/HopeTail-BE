package hello.hello_spring.service;

import hello.hello_spring.domain.Pet;
import hello.hello_spring.dto.pet.PetCreateRequestDto;
import hello.hello_spring.dto.pet.PetResponseDto;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import hello.hello_spring.domain.member.Member;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final MemberRepository memberRepository;

    //JWT 이후에

//    public PetResponseDto createPet(PetCreateRequestDto dto, Member member) {
//        Pet pet = new Pet();
//        pet.setName(dto.getName());
//        pet.setAge(dto.getAge());
//        pet.setGender(dto.getGender());
//        pet.setBreed(dto.getBreed());
//        pet.setDescription(dto.getDescription());
//        pet.setImage(dto.getImage());
//        pet.setStatus(Pet.Status.WAITING);
//        pet.setMember(member);
//
//        Pet saved = petRepository.save(pet);
//        return new PetResponseDto(saved);
//    }

    public PetResponseDto createPet(PetCreateRequestDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new RuntimeException("등록자(member)를 찾을 수 없습니다."));

        Pet pet = new Pet();
        pet.setName(dto.getName());
        pet.setAge(dto.getAge());
        pet.setGender(dto.getGender());
        pet.setBreed(dto.getBreed());
        pet.setDescription(dto.getDescription());
        pet.setImage(dto.getImage());
        pet.setStatus(Pet.Status.WAITING);
        pet.setMember(member); // ✅ 등록자 설정

        Pet saved = petRepository.save(pet);
        return new PetResponseDto(saved);
    }

    public List<PetResponseDto> getAllPets() {
        return petRepository.findByStatus(Pet.Status.WAITING)
                .stream()
                .map(PetResponseDto::new)
                .collect(Collectors.toList());
    }

    public PetResponseDto getPet(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유기견을 찾을 수 없습니다."));
        return new PetResponseDto(pet);
    }
}

