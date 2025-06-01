package hello.hello_spring.service;

import hello.hello_spring.domain.jwt.token.TokenProvider;
import hello.hello_spring.dto.comment.CommentCreateRequestDto;
import hello.hello_spring.dto.comment.CommentResponseDto;
import hello.hello_spring.domain.Comment;
import hello.hello_spring.domain.member.Member;
import hello.hello_spring.domain.Post;
import hello.hello_spring.repository.CommentRepository;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.PostRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    // 댓글 생성
    public CommentResponseDto createComment(Long postId, CommentCreateRequestDto dto,
                                            HttpServletRequest request) {
        String email = extractEmailFromToken(request);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setEmail(email);
        comment.setMember(member);
        comment.setContent(dto.getContent());

        Comment saved = commentRepository.save(comment);
        return new CommentResponseDto(saved);
    }

    // 특정 게시글에 달린 댓글 목록
    public List<CommentResponseDto> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        List<Comment> comments = post.getComments(); // Post 엔티티의 @OneToMany comments
        return comments.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    // 댓글 단건 조회
    public CommentResponseDto getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, HttpServletRequest request) {
        String email = extractEmailFromToken(request);

        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new RuntimeException("Comment not found"));

        if (!comment.getEmail().equals(email)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        commentRepository.deleteById(commentId);
    }

    private String extractEmailFromToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = "";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        Claims claims = tokenProvider.getClaims(token);
        String email = claims.getSubject();



        return email;
    }
}
