package hello.hello_spring.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateDto {
    @Email
    private String email;

    @NotNull
    private String password;

    @NotEmpty
    private String username;

    @NotNull
    private String address;

    @NotNull
    private String phoneNumber;

}
