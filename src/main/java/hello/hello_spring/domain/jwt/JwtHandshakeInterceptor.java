package hello.hello_spring.domain.jwt;

import hello.hello_spring.domain.jwt.token.TokenProvider;
import hello.hello_spring.dto.token.TokenValidationResult;
import hello.hello_spring.domain.jwt.token.TokenStatus;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        log.info("🧩 WebSocket 핸드셰이크 시작됨"); // 1단계: 진입 확인

        URI uri = request.getURI(); // ex: /ws/chat?token=eyJhbGciOi...

        String query = uri.getQuery(); // ex: token=eyJhbGciOi...
        if (query != null && query.startsWith("token=")) {
            String token = query.substring("token=".length());

            log.info("🔑 추출한 토큰: {}", token); // 2단계: 토큰 잘 들어왔는지

            TokenValidationResult result = tokenProvider.validateToken(token);

            log.info("🔍 토큰 상태: {}", result.getTokenStatus()); // ✅ 3단계: VALID인지 확인
            log.info("📧 토큰에서 추출한 이메일: {}", result.getClaims().getSubject()); // ✅ 4단계: 이메일 추출 확인

            if (result.getTokenStatus() == TokenStatus.TOKEN_VALID) {
                Claims claims = result.getClaims();
                String email = claims.getSubject(); // 이메일이 Subject에 저장되어 있음

                attributes.put("userEmail", email);
                return true;
            }
        }

        log.warn("❌ 토큰 없음 또는 인증 실패, 연결 거부");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
    }
}
