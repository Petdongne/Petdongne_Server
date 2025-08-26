package org.songeun.petdongne_server.global.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.global.notification.NotificationService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotiTaskExecutor {

    private final NotificationService notificationService;

    public void executeWithNotification(ThrowingRunnable task, String successMessage, String failureMessage) {
        try {
            task.run();
//            notificationService.sendExceptionNotification(successMessage);
        } catch (Exception e) {
            log.error("작업 수행 중 문제 발생: {}", e.getMessage());
//            notificationService.sendExceptionNotification(failureMessage);
        }
    }

}
