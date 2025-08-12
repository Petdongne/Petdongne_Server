package org.songeun.petdongne_server.global.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ExceptionNotificationAspect {

    private final NotificationService notificationService;

    @Around("@annotation(notifyOnException)")
    public Object notifyOnAnnotatedMethod(ProceedingJoinPoint joinPoint,
                                          NotifyOnException notifyOnException) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Exception exception) {
            if (shouldNotifyException(exception, notifyOnException.excludeExceptions())) {
                String methodName = joinPoint.getSignature().getName();
                String className = joinPoint.getTarget().getClass().getSimpleName();
                String msg = notifyOnException.message();

                notificationService.sendExceptionNotification(
                        exception, methodName, className, msg);
            }
            throw exception;
        }
    }

    private boolean shouldNotifyException(Exception exception,
                                          Class<? extends Exception>[] excludeExceptions) {
        return Arrays.stream(excludeExceptions)
                .noneMatch(excludeType -> excludeType.isInstance(exception));
    }

}
