package org.songeun.petdongne_server.security.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.security.session.SessionData;
import org.songeun.petdongne_server.security.session.SessionStore;
import org.songeun.petdongne_server.user.domain.entity.User;
import org.songeun.petdongne_server.user.infrastructure.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpaqueTokenAuthenticationManager implements AuthenticationManager {

    private final UserRepository userRepository;
    private final SessionStore sessionStore;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof BearerTokenAuthenticationToken)) {
            throw new AuthenticationServiceException(
                    "Unsupported authentication type: " + authentication.getClass().getName()
            );
        }
        BearerTokenAuthenticationToken bearerToken = (BearerTokenAuthenticationToken) authentication;
        String token = bearerToken.getValue(); // opaqueToken
        String sessionId = token;
        SessionData session = getSessionOrThrow(sessionId);
        UserPrincipal userPrincipal = createUserPrincipalOrThrow(session);

        return new OpaqueTokenAuthenticationToken(
                token,
                userPrincipal,
                userPrincipal.getAuthorities()
        );

    }

    private SessionData getSessionOrThrow(String sessionId) {
        SessionData sessionData = null;
        try {
            sessionData = sessionStore.getSession(sessionId);
            if (sessionData == null) {
                throw new BadCredentialsException("유효하지 않거나 만료된 토큰입니다.");
            }
            return sessionData;
        } catch (JsonProcessingException e) {
            log.error("세션 데이터 파싱 실패: {}", e.getMessage(), e);
            throw new AuthenticationServiceException("인증 서버 문제로 인증에 실패했습니다.");

        }
    }

    private UserPrincipal createUserPrincipalOrThrow(SessionData session) {
        User user = userRepository.findById(session.userId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserPrincipal(user);
    }

}
