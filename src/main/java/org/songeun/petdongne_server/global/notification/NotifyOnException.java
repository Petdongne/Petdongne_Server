package org.songeun.petdongne_server.global.notification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotifyOnException {

    String message() default "";
    Class<? extends Exception>[] excludeExceptions() default {};

}
