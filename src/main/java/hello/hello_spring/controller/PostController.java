package hello.hello_spring.controller;

import hello.hello_spring.dto.post.PostCreateRequestDto;
import hello.hello_spring.dto.post.PostResponseDto;
import hello.hello_spring.dto.post.PostUpdateRequestDto;
import hello.hello_spring.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 생성")
    @PostMapping
    public PostResponseDto createPost(@RequestBody PostCreateRequestDto dto, HttpServletRequest request) {
        return postService.createPost(dto, request);
    }

    @Operation(summary = "게시글 전체 조회")
    @GetMapping
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @Operation(summary = "게시글 단건 조회")
    @GetMapping("/{postId}")
    public PostResponseDto getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

    @Operation(summary = "게시글 수정")
    @PutMapping("/{postId}")
    public PostResponseDto updatePost(@PathVariable Long postId,
                                      @RequestBody PostUpdateRequestDto dto,
                                      HttpServletRequest request) {
        return postService.updatePost(postId, dto, request);
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId, HttpServletRequest request) {
        postService.deletePost(postId, request);
    }

    @Operation(summary = "카테고리별 게시글 조회")
    @GetMapping("/category")
    public List<PostResponseDto> getPostsByCategory(@RequestParam String category) {
        return postService.getPostsByCategory(category);
    }
}
