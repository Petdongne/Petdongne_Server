package org.songeun.petdongne_server.global.common;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.global.notification.NotificationService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskExecutor {

    private final NotificationService notificationService;

    public void executeWithNotification(ThrowingRunnable task, String successMessage, String failureMessage) {
        try {
            task.run();
            notificationService.notify(successMessage);
        } catch (Exception e) {
            notificationService.notify(failureMessage);
        }
    }

}
