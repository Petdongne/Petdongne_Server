package org.songeun.petdongne_server.global.file.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CsvFileValidator implements ConstraintValidator<CsvFile, MultipartFile> {

    private final Tika tika;

    private String[] allowedExtensions;
    private String[] allowedMimeTypes;
    private int maxSizeMB;

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        // 위치 고민 -> 별도의 커스텀 애노테이션 or 서비스
        if (file == null || file.isEmpty()) {
            return false;
        }

        // 1. 확장자 검증
        String filename = file.getOriginalFilename();
        if (filename == null || allowedExtensions.length == 0 ||
                java.util.Arrays.stream(allowedExtensions)
                        .noneMatch(ext -> filename.toLowerCase().endsWith(ext))) {
            return false;
        }

        // 2. MIME 타입 검증 with Tika
        try {
            String detectedMimeType = tika.detect(file.getInputStream());
            if (java.util.Arrays.stream(allowedMimeTypes)
                    .noneMatch(mime -> mime.equalsIgnoreCase(detectedMimeType))) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }

        // 3. 파일 크기 제한
        long maxSizeBytes = maxSizeMB * 1024L * 1024L;

        return file.getSize() <= maxSizeBytes;
    }

    @Override
    public void initialize(CsvFile constraintAnnotation) {
        this.allowedExtensions = constraintAnnotation.allowedExtensions();
        this.allowedMimeTypes = constraintAnnotation.allowedMimeTypes();
        this.maxSizeMB = constraintAnnotation.maxSizeMB();
    }

}
