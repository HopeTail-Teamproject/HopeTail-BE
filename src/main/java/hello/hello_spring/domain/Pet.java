package hello.hello_spring.domain;

import hello.hello_spring.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "유기견 이름")
    @Column(nullable = false)
    private String name;

    @Schema(description = "유기견 나이")
    private int age;

    @Schema(description = "유기견 성별 (MALE / FEMALE)")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Schema(description = "유기견 종 (ex. 말티즈, 진돗개)")
    private String species;

    @Schema(description = "중성화 여부")
    private boolean neutered;

    @Schema(description = "성격 및 건강상태&히스토리(사연)")
    @Column(columnDefinition = "TEXT")
    private String description;

    @Schema(description = "배변 교육 여부")
    private boolean trained;

    @Schema(description = "예방접종 여부")
    private boolean vaccinated;

    private String image; // 이미지 URL or 경로

    @Enumerated(EnumType.STRING)
    private Status status = Status.WAITING;

    @ManyToOne
    @JoinColumn(name = "registered_by", nullable = false)
    private Member registeredBy;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Adoption> adoptions;

    public enum Gender {
        MALE, FEMALE
    }

    public enum Status {
        WAITING, ADOPTED
    }

}
