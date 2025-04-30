package hello.hello_spring.dto.login;

import hello.hello_spring.dto.token.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private TokenDto accessToken;

    private TokenDto refreshToken;

}
