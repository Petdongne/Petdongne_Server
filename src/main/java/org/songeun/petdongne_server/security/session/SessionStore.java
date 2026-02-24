package org.songeun.petdongne_server.security.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.songeun.petdongne_server.security.session.SessionConfig.SESSION_TIMEOUT_MINUTES;

@Component
@RequiredArgsConstructor
public class SessionStore {

    private static final String SESSION_ID_PREFIX = "session:";

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Nullable
    public SessionData getSession(String sessionId) throws JsonProcessingException {
        String sessionIdWithPrefix = SESSION_ID_PREFIX + sessionId;

        String sessionStr = stringRedisTemplate.opsForValue().get(sessionIdWithPrefix);
        if (sessionStr == null || sessionStr.isEmpty()) {
            return null;
        }
        return objectMapper.readValue(sessionStr, SessionData.class);
    }

    public void resetSessionExpiration(String sessionId) {
        String sessionIdWithPrefix = SESSION_ID_PREFIX + sessionId;
        stringRedisTemplate.expire(sessionIdWithPrefix, SESSION_TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    public void saveSession(String sessionId, SessionData sessionData) throws JsonProcessingException {
        String sessionIdWithPrefix = SESSION_ID_PREFIX + sessionId;
        String sessionJson = objectMapper.writeValueAsString(sessionData);

        stringRedisTemplate.opsForValue().set(
                sessionIdWithPrefix,
                sessionJson,
                SESSION_TIMEOUT_MINUTES,
                TimeUnit.MINUTES
        );
    }

}
