package hello.hello_spring.domain.member;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class MemberPrinciple extends User {
    private final String email;
    private final String accessToken;
    private static final String PASSWORD_ERASED_VALUE = "{PASSWORD_ERASED_VALUE}";
    public MemberPrinciple(String username, String email, String accessToken, Collection<? extends GrantedAuthority> authorities) {
        super(username, PASSWORD_ERASED_VALUE, authorities);
        this.accessToken = accessToken;
        this.email = email;
    }

    @Override
    public String toString() {
        return "MemberPrinciple{" +
                "email='" + email +
                "username='" + getUsername() +
                "role='" + getAuthorities() +
                '}';
    }

}
