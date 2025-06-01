package hello.hello_spring.dto.post;

import hello.hello_spring.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDto {
    @Schema(description = "게시글 ID", example = "42")
    private Long id;

    @Schema(description = "게시글 제목", example = "임시보호 후기 공유합니다")
    private String title;

    @Schema(description = "게시글 내용", example = "오늘 아이가 처음으로 사료를 잘 먹었어요!")
    private String content;

    @Schema(description = "카테고리", example = "DIARY or REVIEW")
    private String category;

    @Schema(description = "생성 일시", example = "2025-05-02T15:20:30")
    private LocalDateTime createdAt;

    @Schema(description = "작성자 멤버 ID", example = "7")
    private Long memberId;

    @Schema(description = "작성자 이메일", example = "user@example.com")
    private String email;

    // 엔티티 -> DTO 변환
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory().name();
        this.createdAt = post.getCreatedAt();
        this.email = post.getEmail();
//        this.memberId = post.getMember().getId();
    }
}
