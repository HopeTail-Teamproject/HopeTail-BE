package hello.hello_spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*")
                .allowedOrigins(
                        "http://localhost:8080/",   // 로컬 테스트
                        "https://hopetail-teamproject.github.io/",  // GitHub Pages
                        "https://api.hopetail.com/"             // API 서버 주소 (HTTPS)
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("")                         // 모든 헤더 허용
                .allowCredentials(true);                     // 쿠키 허용
    }
}