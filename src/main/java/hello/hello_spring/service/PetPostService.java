package hello.hello_spring.service;

import hello.hello_spring.domain.jwt.token.TokenProvider;
import hello.hello_spring.domain.petPost.PetPost;
import hello.hello_spring.dto.pet.PetPostCreateRequestDto;
import hello.hello_spring.dto.pet.PetPostResponseDto;
import hello.hello_spring.repository.PetPostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetPostService {

    private final PetPostRepository repo;
    private final TokenProvider tokenProvider;

    public PetPostResponseDto createPet(PetPostCreateRequestDto dto, HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = "";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        Claims claims = tokenProvider.getClaims(token);
        String email = claims.getSubject();

        // 3) 엔티티 생성 & email 세팅
        PetPost post = new PetPost();
        post.setPhotoUrl(dto.getPhotoUrl());
        post.setName(dto.getName());
        post.setAge(dto.getAge());
        post.setSpecies(dto.getSpecies());
        post.setAddress(dto.getAddress());
        post.setDescription(dto.getDescription());
        post.setEmail(email);                    // ← 여기서 이메일로 식별

        // 4) 저장
        PetPost saved = repo.save(post);
        return new PetPostResponseDto(saved);
    }

    public List<PetPostResponseDto> getAllPets() {
        return repo.findAll().stream()
                .map(PetPostResponseDto::new)
                .collect(Collectors.toList());
    }


    public PetPostResponseDto getPet(Long petId) {
        PetPost pet = repo.findById(petId)
                .orElseThrow(()->new RuntimeException("Pet id 없음"));
        return new PetPostResponseDto(pet);
    }


    public void deletePet(Long petId, HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = "";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        Claims claims = tokenProvider.getClaims(token);
        String email = claims.getSubject();

        PetPost pet = repo.findById(petId)
                .orElseThrow(()->new RuntimeException("Pet id 없음"));
        if(!pet.getEmail().equals(email)){
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        repo.deleteById(petId);
    }
}
