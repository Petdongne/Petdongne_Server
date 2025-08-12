package org.songeun.petdongne_server.global.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.exception.SystemException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final DiscordNotifier notifier;

    public void sendExceptionNotification(Exception exception, String className, String methodName, String message) {
        String detailedMessage = buildExceptionMessage(exception, className, methodName, message);

        ResponseEntity response = notifier.send(detailedMessage);

        if (response.getStatusCode().isError()) {
            log.error("Discord 알림 전송에 실패했습니다. Status: {}", response.getStatusCode());
            throw new SystemException(GlobalErrorStatus.FAIL_SEND_DISCORD_MESSAGE);
        }

        log.debug("Discord 알림 전송 완료");
    }

    private String buildExceptionMessage(Exception exception, String className, String methodName, String message) {
        StringBuilder sb = new StringBuilder();

        // 기본 메시지
        if (message != null && !message.isEmpty()) {
            sb.append("📢 **").append(message).append("**\n\n");
        } else {
            sb.append("🚨 **예외 발생 알림**\n\n");
        }

        // 예외 정보
        sb.append("```\n");
        sb.append("클래스: ").append(className).append("\n");
        sb.append("메서드: ").append(methodName).append("\n");
        sb.append("예외 타입: ").append(exception.getClass().getSimpleName()).append("\n");
        sb.append("예외 메시지: ").append(exception.getMessage() != null ? exception.getMessage() : "없음").append("\n");
        sb.append("발생 시간: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        sb.append("```\n");

        // 스택 트레이스 (처음 5줄만)
        String stackTrace = getSimpleStackTrace(exception);
        if (!stackTrace.isEmpty()) {
            sb.append("\n**스택 트레이스:**\n");
            sb.append("```\n");
            sb.append(stackTrace);
            sb.append("```");
        }

        return sb.toString();
    }

    private String getSimpleStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);

        String fullStackTrace = sw.toString();
        String[] lines = fullStackTrace.split("\n");

        StringBuilder result = new StringBuilder();
        int maxLines = Math.min(5, lines.length);

        for (int i = 0; i < maxLines; i++) {
            result.append(lines[i]).append("\n");
        }

        if (lines.length > 5) {
            result.append("... (생략됨)");
        }

        return result.toString();
    }

}
