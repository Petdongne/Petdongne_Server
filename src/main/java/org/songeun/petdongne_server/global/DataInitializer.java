package org.songeun.petdongne_server.global;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.security.session.SessionData;
import org.songeun.petdongne_server.security.session.SessionStore;
import org.songeun.petdongne_server.user.domain.entity.AuthenticationProvider;
import org.songeun.petdongne_server.user.domain.entity.User;
import org.songeun.petdongne_server.user.infrastructure.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Profile("dev")
@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final SessionStore sessionStore;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = createOrGet("dev@example.com");

        SessionData sessionData = new SessionData(user.getId());

        String sessionId = String.valueOf(UUID.randomUUID());
        sessionStore.saveSession(sessionId, sessionData);

        log.info("Created Session id for developer: {}", sessionId);
    }

    private User createOrGet(String mail) {
        Optional<User> userOpt = userRepository.findByEmail(mail);
        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else{
            User created = User.builder()
                    .email(mail)
                    .nickname("developer")
                    .identifierFromProvider(UUID.randomUUID().toString())
                    .authenticationProvider(AuthenticationProvider.KAKAO)
                    .build();
            User saved = userRepository.save(created);
            user = saved;
        }

        return user;
    }
}
