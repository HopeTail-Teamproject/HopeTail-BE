package hello.hello_spring.config;

import hello.hello_spring.domain.jwt.token.TokenProvider;
import hello.hello_spring.domain.jwt.JwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final TokenProvider tokenProvider;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat")
                .addInterceptors(new JwtHandshakeInterceptor(tokenProvider))
                .setAllowedOriginPatterns("*")
                // .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub"); // 클라이언트 요청
        registry.enableSimpleBroker("/sub"); // 서버 → 클라이언트
    }

    @Override // 인바운드 채널
    public void configureClientInboundChannel(ChannelRegistration reg) {
        reg.taskExecutor().corePoolSize(4).maxPoolSize(16);
    }

}
