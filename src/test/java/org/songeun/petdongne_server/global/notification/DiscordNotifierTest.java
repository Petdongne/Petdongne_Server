package org.songeun.petdongne_server.global.notification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.songeun.petdongne_server.global.exception.SystemException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class DiscordNotifierTest {

    @Mock
    private RestClient discordClient;

    @InjectMocks
    private DiscordNotifier notifier;

    @Test
    @DisplayName("메시지 전송 실패 시 예외를 던진다.")
    void test() {
        //given
        String message = "안뇽";
        given(discordClient.post()
                        .body(Map.of("content", message))
                        .retrieve()
                        .toBodilessEntity())
                .willReturn((ResponseEntity<Void>) ResponseEntity.of(ProblemDetail.forStatus(HttpStatusCode.valueOf(500))));

        //when & then
        assertThatThrownBy(() -> notifier.send(message))
                .isInstanceOf(SystemException.class);
    }

}