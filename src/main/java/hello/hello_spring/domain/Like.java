package hello.hello_spring.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "member_id", "post_id" }) }) // 중복 좋아요 방지
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
