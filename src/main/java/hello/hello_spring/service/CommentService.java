package hello.hello_spring.service;

import hello.hello_spring.dto.comment.CommentCreateRequestDto;
import hello.hello_spring.dto.comment.CommentResponseDto;
import hello.hello_spring.domain.Comment;
import hello.hello_spring.domain.member.Member;
import hello.hello_spring.domain.Post;
import hello.hello_spring.repository.CommentRepository;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 댓글 생성
    public CommentResponseDto createComment(Long postId, CommentCreateRequestDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Comment comment = new Comment();
        comment.setPost(post);
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
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
