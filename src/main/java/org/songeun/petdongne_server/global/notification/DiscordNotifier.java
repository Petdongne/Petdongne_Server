package org.songeun.petdongne_server.global.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class DiscordNotifier {

    public static final String MESSAGE_CONTENT_FIELD = "content";

    private final RestClient discordClient;

    public ResponseEntity send(String message) {
        Map<String, String> payload = Map.of(MESSAGE_CONTENT_FIELD, message);

        return discordClient.post()
                .body(payload)
                .retrieve()
                .toBodilessEntity();
    }

}
