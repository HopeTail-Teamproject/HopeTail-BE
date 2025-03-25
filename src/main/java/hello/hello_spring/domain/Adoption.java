package hello.hello_spring.domain;

import hello.hello_spring.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 입양 신청한 유기견
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    // 신청자
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private Status status = Status.REQUESTED;

    private LocalDateTime appliedAt = LocalDateTime.now();

    public enum Status {
        REQUESTED, APPROVED, REJECTED
    }
}
