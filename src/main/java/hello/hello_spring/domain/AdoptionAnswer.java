package hello.hello_spring.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "adoption_request_id", nullable = false)
    private AdoptionRequest adoptionRequest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdoptionQuestionType questionType;

    // 해당 질문에 대한 사용자 답변
    @Column(columnDefinition = "TEXT")
    private String answer;
}
