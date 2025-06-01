package hello.hello_spring.service;

import hello.hello_spring.domain.Post;
import hello.hello_spring.domain.PostLike;
import hello.hello_spring.domain.jwt.token.TokenProvider;
import hello.hello_spring.domain.member.Member;
import hello.hello_spring.domain.petPost.PetPost;
import hello.hello_spring.domain.petPost.PetPostLike;
import hello.hello_spring.dto.pet.PetPostCreateRequestDto;
import hello.hello_spring.dto.pet.PetPostResponseDto;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.PetPostLikeRepository;
import hello.hello_spring.repository.PetPostRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
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
    private final MemberRepository memberRepository;
    private final PetPostLikeRepository petPostLikeRepository;

    @Transactional
    public void createPet(PetPostCreateRequestDto dto, String imageUrl ,HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = "";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        Claims claims = tokenProvider.getClaims(token);
        String email = claims.getSubject();

        // 3) 엔티티 생성 & email 세팅
        PetPost post = new PetPost();
        post.setPhotoUrl(imageUrl);
        post.setName(dto.getName());
        post.setAge(dto.getAge());
        post.setSpecies(dto.getSpecies());
        post.setAddress(dto.getAddress());
        post.setDescription(dto.getDescription());
        post.setEmail(email);                    // ← 여기서 이메일로 식별

        // 4) 저장
        repo.save(post);
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

    public void handlePetPostLikeButton(Long petId, HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = "";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        Claims claims = tokenProvider.getClaims(token);
        String email = claims.getSubject();

        PetPost petPost = repo.findById(petId).orElseThrow(
                () -> new RuntimeException("삭제된 펫입니다."));

        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        PetPostLike petPostLike = petPostLikeRepository.findByPetPostIdAndEmail(petId, email);

        if (petPostLike == null) {
            petPostLike = new PetPostLike();
            petPostLike.setPetPost(petPost);
            petPostLike.setEmail(email);
            petPostLike.setMember(member);
            petPostLikeRepository.save(petPostLike);
        }
        else {
            petPostLikeRepository.delete(petPostLike);
        }

    }
}

