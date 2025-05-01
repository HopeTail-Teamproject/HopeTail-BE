package hello.hello_spring.domain.petPost;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    public enum Status {
        PROTECTING, ADOPTED
    }
}
