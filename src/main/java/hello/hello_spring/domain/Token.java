package hello.hello_spring.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String Token;

    @CreationTimestamp
    private LocalDateTime createdat;

    private LocalDateTime expiredat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    private CertificationType certificationType;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    public enum Type {
        REFRESH,
        CERTIFICATION,
        PASSWORD_RESET
    }

    public enum CertificationType {
        EMAIL,
        PHONE
    }
}




