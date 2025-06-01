package hello.hello_spring.domain.jwt.blacklist;

import hello.hello_spring.domain.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessTokenBlackList {

    private final RedisTemplate<String, Object> redisBlackListTemplate;

    private final JwtProperties jwtProperties;

    public void setBlackList(String key, Object o) {
        redisBlackListTemplate.opsForValue().set(key, o, jwtProperties.getAccessTokenValidityInSeconds(), TimeUnit.SECONDS);
    }

    public Object getBlackList(String key) {
        return redisBlackListTemplate.opsForValue().get(key);
    }


    public boolean isBlackList(String key) {
        return Boolean.TRUE.equals(redisBlackListTemplate.hasKey(key));
    }
}
