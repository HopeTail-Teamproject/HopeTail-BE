package hello.hello_spring.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginDto {

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private boolean rememberMe;
}
