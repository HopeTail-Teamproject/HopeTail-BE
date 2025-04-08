//package hello.hello_spring.domain.jwt.token;
//
//import hello.hello_spring.domain.member.Member;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.util.Date;
//
//@Entity
//@Setter
//@Getter
//@NoArgsConstructor
//@ToString(exclude = {"Token"})
//public class Token {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column
//    private String tokenId;
//
//    @Lob
//    private String Token;
//
//    @CreationTimestamp
//    private Date tokenCreatedAt;
//
//    private Date tokenExpiredAt;
//
//    @Email
//    private String email;
//
//    @ManyToOne
//    @JoinColumn(name = "member_id", nullable = false)
//    private Member member;
//
//
//}
//
//
//

