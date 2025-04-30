package hello.hello_spring.service;

import hello.hello_spring.domain.Post;
import hello.hello_spring.domain.jwt.token.TokenProvider;
import hello.hello_spring.dto.post.PostCreateRequestDto;
import hello.hello_spring.dto.post.PostResponseDto;
import hello.hello_spring.dto.post.PostUpdateRequestDto;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    // 게시글 생성
    public PostResponseDto createPost(PostCreateRequestDto dto, HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        String token = "";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        Claims claims = tokenProvider.getClaims(token);
        String email = claims.getSubject();

        // 2. 엔티티 생성
        Post post = new Post();
        post.setEmail(email);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(Post.Category.valueOf(dto.getCategory().toUpperCase()));

        // 3. 저장
        Post savedPost = postRepository.save(post);

        // 4. 응답 DTO 반환
        return new PostResponseDto(savedPost);
    }

    // 게시글 전체 조회
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    // 게시글 단건 조회
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return new PostResponseDto(post);
    }

    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto dto, HttpServletRequest request) {
        // 1. 토큰에서 이메일 추출
        String bearerToken = request.getHeader("Authorization");
        String token = "";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        Claims claims = tokenProvider.getClaims(token);
        String emailFromToken = claims.getSubject();

        // 2. 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 3. 이메일이 일치하지 않으면 예외
        if (!post.getEmail().equals(emailFromToken)) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }

        // 4. 수정
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(Post.Category.valueOf(dto.getCategory().toUpperCase()));
        Post updated = postRepository.save(post);

        return new PostResponseDto(updated);
    }

    //게시물 삭제
    public void deletePost(Long postId, HttpServletRequest request) {
        // 1. 토큰에서 이메일 추출
        String bearerToken = request.getHeader("Authorization");
        String token = "";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        Claims claims = tokenProvider.getClaims(token);
        String email = claims.getSubject();

        // 2. 게시글 가져오기
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 3. 본인 글인지 확인
        if (!post.getEmail().equals(email)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        // 4. 삭제
        postRepository.deleteById(postId);
    }


    // 카테고리별 게시글 조회
    public List<PostResponseDto> getPostsByCategory(String category) {
        List<Post> posts = postRepository.findByCategory(Post.Category.valueOf(category.toUpperCase()));
        return posts.stream().map(PostResponseDto::new).collect(Collectors.toList());
    }
}
