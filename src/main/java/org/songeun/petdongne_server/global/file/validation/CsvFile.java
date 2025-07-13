package org.songeun.petdongne_server.global.file.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CsvFileValidator.class)
public @interface CsvFile {

    String[] allowedExtensions() default {".csv"};
    String[] allowedMimeTypes() default {"text/csv"};
    int maxSizeMB() default 10;
    String message() default "Invalid CSV file";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
