package hello.hello_spring.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private String profileImage; // nullable

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    // 관계 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Adoption> adoptions;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetRegistration> petRegistrations;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> Tokens; // 한 사용자가 여러 토큰 가질 수 있음    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private AuthToken authToken;

    // 사용자 권한 enum
    public enum Role {
        USER, ADMIN
    }
}

enum Status {
    ACTIVE,
    INACTIVE,
    PENDING                  // 인증 받기 전 PENDING 으로 대기 상태 유지
}