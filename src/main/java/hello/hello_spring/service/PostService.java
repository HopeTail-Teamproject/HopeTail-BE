package hello.hello_spring.service;

import hello.hello_spring.dto.*;
import hello.hello_spring.domain.Member;
import hello.hello_spring.domain.Post;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 게시글 생성
    public PostResponseDto createPost(PostCreateRequestDto dto) {
        // 1. 작성자(Member) 조회
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 2. 엔티티 생성
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setMember(member);
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

    // 게시글 수정
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(Post.Category.valueOf(dto.getCategory().toUpperCase()));
        Post updated = postRepository.save(post);
        return new PostResponseDto(updated);
    }

    // 게시글 삭제
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    // 카테고리별 게시글 조회
    public List<PostResponseDto> getPostsByCategory(String category) {
        List<Post> posts = postRepository.findByCategory(Post.Category.valueOf(category.toUpperCase()));
        return posts.stream().map(PostResponseDto::new).collect(Collectors.toList());
    }
}
