package hello.hello_spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 구독할 destination prefix 지정
        registry.enableSimpleBroker("/topic", "/queue");
        // 클라이언트가 메시지를 보낼 때 붙일 prefix 지정
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 웹소켓 연결 엔드포인트 설정 (SockJS 지원 포함)
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*") // 모든 Origin 허용 (개발 환경)
                .withSockJS();
    }
}
