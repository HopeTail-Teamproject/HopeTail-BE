package hello.hello_spring.dto;

import hello.hello_spring.domain.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String category;
    private LocalDateTime createdAt;
    //private Long memberId; // 작성자 ID

    // 엔티티 -> DTO 변환
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory().name();
        this.createdAt = post.getCreatedAt();
        //this.memberId = post.getMember().getId();
    }
}
