package hello.hello_spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:5173",
                        "http://localhost:8080",   // 로컬 테스트
                        "https://hopetail-teamproject.github.io",  // GitHub Pages
                        "https://api.hopetail.com"             // API 서버 주소 (HTTPS)
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")                         // 모든 헤더 허용
                .allowCredentials(true);                     // 쿠키 허용
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {
            // "./images" 이렇게 상대 경로로 폴더 위치를 가져옴
            // 이후 "/Users/you/project/images" 처럼 절대 경로로 변환
            Path imageDir = Paths.get("images").toAbsolutePath();

            // 폴더가 존재하지 않으면 생성
            if (Files.notExists(imageDir)) {
                Files.createDirectories(imageDir);
            }

            // "/images"로 시작하는 url에 "images" 폴더 안에 있는 사진들을 매핑
            registry.addResourceHandler("/images/**")
                    .addResourceLocations(imageDir.toUri().toString());

        } catch (IOException e) {
            throw new RuntimeException("이미지 디렉토리를 생성하거나 설정하는 중 오류 발생", e);
        }
    }
}