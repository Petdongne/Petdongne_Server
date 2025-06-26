package org.songeun.petdongne_server.global.util;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.nio.charset.StandardCharsets;

public class EncodingValidator {

    private static final Tika tika = new Tika();

    /**
     * 1. .csv 확장자 확인
     */
    private static void validateExtension(MultipartFile file) {
        String name = file.getOriginalFilename();
        if (name == null || !name.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("CSV 파일만 업로드할 수 있습니다.");
        }
    }

    /**
     * 2. Tika로 MIME 타입 감지
     */
    private static void validateMimeType(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String detected = tika.detect(inputStream, file.getOriginalFilename());
            if (!detected.equals("text/csv")) {
                throw new IllegalArgumentException("CSV 파일이 아닙니다. 감지된 타입: " + detected);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 내용을 읽을 수 없습니다.", e);
        }
    }

    /**
     * 3. UTF-8 또는 UTF-8 with BOM 인코딩 검사
     */
    private static void validateEncoding(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            PushbackInputStream pbStream = new PushbackInputStream(inputStream, 3);
            byte[] bom = new byte[3];
            int read = pbStream.read(bom, 0, 3);

            boolean hasBom = read == 3 && bom[0] == (byte) 0xEF
                    && bom[1] == (byte) 0xBB
                    && bom[2] == (byte) 0xBF;

            if (!hasBom) {
                pbStream.unread(bom, 0, read); // 다시 돌려놓기
            }

            // 본격적인 UTF-8 유효성 검사
            try (InputStreamReader reader = new InputStreamReader(pbStream, StandardCharsets.UTF_8)) {
                char[] buffer = new char[1024];
                while (reader.read(buffer) != -1) {
                    // 읽기 성공 = UTF-8 문제 없음
                }
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("UTF-8 인코딩의 CSV 파일만 허용됩니다.", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("UTF-8 인코딩이 아닌 파일입니다.");
        }
    }

}
