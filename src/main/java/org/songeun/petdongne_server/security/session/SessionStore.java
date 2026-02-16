package org.songeun.petdongne_server.security.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.songeun.petdongne_server.security.session.SessionConfig.SESSION_TIMEOUT_MINUTES;

@Component
@RequiredArgsConstructor
public class SessionStore {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public String getSession(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void resetSessionExpiration(String sessionId) {
        stringRedisTemplate.expire(sessionId, SESSION_TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    public void saveSession(String sessionId, SessionData sessionData) throws JsonProcessingException {
        String sessionJson = objectMapper.writeValueAsString(sessionData);

        stringRedisTemplate.opsForValue().set(
                sessionId,
                sessionJson,
                SESSION_TIMEOUT_MINUTES,
                TimeUnit.MINUTES
        );
    }

}
