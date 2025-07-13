package org.songeun.petdongne_server.global.file.validation;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import org.songeun.petdongne_server.global.common.AllowedCharset;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@Component
public class CharsetValidator {

    public void validateCharset(MultipartFile file, AllowedCharset expectedCharset) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Uploaded file is empty");
        }

        try (InputStream inputStream = file.getInputStream()) {
            CharsetMatch match = detectCharset(inputStream);
            Charset detectedCharset = toCharset(match);

            if (!isEqual(expectedCharset, detectedCharset)) {
                throw new IOException("File '" + file.getOriginalFilename() +
                        "' charset validation failed. Expected: " + expectedCharset.name() +
                        ", Detected: " + detectedCharset.name());
            }
        }
    }

    private CharsetMatch detectCharset(InputStream inputStream) throws IOException {
        CharsetDetector detector = new CharsetDetector();
        detector.setText(inputStream);

        CharsetMatch detected = detector.detect();
        if (detected == null) {
            throw new IOException();
        }

        return detected;
    }

    private Charset toCharset(CharsetMatch match) {
        return Charset.forName(match.getName());
    }

    private boolean isEqual(AllowedCharset charset, Charset matchCharset) {
        return matchCharset.equals(charset.getIanaCharset());
    }

}
