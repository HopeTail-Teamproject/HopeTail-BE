package hello.hello_spring.domain;

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

    @Column(nullable = false)
    private String name;

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String breed;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image; // 이미지 URL or 파일 경로

    @Enumerated(EnumType.STRING)
    private Status status = Status.WAITING;

    // 등록한 사람 (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "registered_by", nullable = false)
    private Member registeredBy;

    // 입양 신청들 (OneToMany)
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Adoption> adoptions;

    public enum Gender {
        MALE, FEMALE
    }

    public enum Status {
        WAITING, ADOPTED
    }
}
