package hello.hello_spring.domain.jwt.config;

import hello.hello_spring.domain.jwt.JwtAccessDeniedHandler;
import hello.hello_spring.domain.jwt.JwtAuthenticationEntryPoint;
import hello.hello_spring.domain.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("local")
public class LocalSecurityConfig extends SecurityConfig {


    public LocalSecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler, JwtFilter jwtFilter) {
        super(jwtAuthenticationEntryPoint, jwtAccessDeniedHandler, jwtFilter);
    }
    @Bean
    @Override
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.requiresChannel(channel ->
                channel.anyRequest().requiresInsecure()) // HTTP 설정
                .build();
    }
}



// .requiresChannel(channel -> channel
//                        .anyRequest().requiresSecure()
//                )