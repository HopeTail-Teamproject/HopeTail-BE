package hello.hello_spring.domain.petPost;

import hello.hello_spring.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PetPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photoUrl;
    private String name;
    private Integer age;
    private String species;
    private String address;

    private LocalDateTime createdAt = LocalDateTime.now();

    //등록한 멤버 이메일
    @Column
    private String email;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PROTECTING;

    @OneToMany(mappedBy = "petPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetPostLike> petPostLikes;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "member_id")
    private Member member;

    public enum Status {
        PROTECTING, ADOPTED
    }
}
