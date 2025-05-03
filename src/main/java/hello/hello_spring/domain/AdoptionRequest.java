package hello.hello_spring.domain;

import hello.hello_spring.domain.member.Member;
import hello.hello_spring.domain.petPost.PetPost;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 신청자
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 대상 유기견
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private PetPost pet;

    @Enumerated(EnumType.STRING)
    private Status status = Status.IN_PROGRESS;

    private LocalDateTime submittedAt;

    public enum Status {
        IN_PROGRESS, SUBMITTED, APPROVED, REJECTED
    }

    @PrePersist
    public void prePersist() {
        this.submittedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = Status.IN_PROGRESS;
        }
    }
}
