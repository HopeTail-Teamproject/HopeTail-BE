package hello.hello_spring.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment 적용
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @CreationTimestamp
    private LocalDateTime createdat;

    @UpdateTimestamp
    private LocalDateTime updatedat;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("PENDING")
    private Status status;

    @Column
    private LocalDateTime lastLogin;

    @ColumnDefault("0")
    private boolean emailCertified;

    @ColumnDefault("0")
    private boolean phoneCertified;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts; // 한 사용자가 여러 게시글 작성 가능

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> Tokens; // 한 사용자가 여러 토큰 가질 수 있음
}

enum Status {
    ACTIVE,
    INACTIVE,
    PENDING                  // 인증 받기 전 PENDING 으로 대기 상태 유지
}