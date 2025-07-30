package org.songeun.petdongne_server.global.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.exception.SystemException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final DiscordNotifier notifier;

    public void notify(final String message) {
        ResponseEntity response = notifier.send(message);

        if (response.getStatusCode().isError()) {
            log.error("Discord 알림 전송에 실패했습니다. Status: {}", response.getStatusCode());
            throw new SystemException(GlobalErrorStatus.FAIL_SEND_DISCORD_MESSAGE);
        }

        log.debug("Discord 알림 전송 완료");
    }

}
