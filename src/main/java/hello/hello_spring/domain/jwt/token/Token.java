package hello.hello_spring.domain.jwt.token;

import hello.hello_spring.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString(exclude = {"Token"})
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String Token;

    @CreationTimestamp
    private LocalDateTime tokenCreatedAt;

    private LocalDateTime tokenExpiredAt;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Email
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;



    public enum Type {
        ACCESS,
        REFRESH,
        CERTIFICATION,
        PASSWORD_RESET
    }

}




