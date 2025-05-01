package hello.hello_spring.domain;

import hello.hello_spring.domain.member.Member;
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

    private String image;

    @Enumerated(EnumType.STRING)
    private Status status = Status.WAITING;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public enum Gender {
        MALE, FEMALE
    }

    public enum Status {
        WAITING, ADOPTED
    }
}

