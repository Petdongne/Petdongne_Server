package org.songeun.petdongne_server.global.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ZipExtractorTest {

    @TempDir
    Path tempDir;

    private void createTestZip(Path zipPath, Map<String, String> entries) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            for (Map.Entry<String, String> entry : entries.entrySet()) {
                ZipEntry zipEntry = new ZipEntry(entry.getKey());
                zos.putNextEntry(zipEntry);

                if (entry.getValue() != null) {
                    zos.write(entry.getValue().getBytes(StandardCharsets.UTF_8));
                }

                zos.closeEntry();
            }

        }
    }
    
    @Test
    @DisplayName("")
    void shouldSuccessWhenUnzip() throws IOException {
        //given
        Path zipFile = tempDir.resolve("test.zip");
        Path outputDir = tempDir.resolve("output");

        Map<String, String> zipContent = new LinkedHashMap<>();
        zipContent.put("addresses/", null); // 디렉토리
        zipContent.put("address_B.csv", "법정동 주소 파일 내용입니다.");
        zipContent.put("address_H.csv", "행정동 주소 파일 내용입니다.");

        createTestZip(zipFile, zipContent);

        //when
        ZipExtractor.unzip(zipFile, outputDir);
        
        //then
        Path file1 = outputDir.resolve("address_B.csv");
        Path file2 = outputDir.resolve("address_H.csv");
        Path dir1 = outputDir.resolve("addresses/");

        assertThat(Files.exists(outputDir)).isTrue();

        assertThat(Files.exists(dir1)).isTrue();
        assertThat(Files.exists(file1)).isTrue();
        assertThat(Files.exists(file2)).isTrue();

        assertThat(Files.readString(file1)).isEqualTo("법정동 주소 파일 내용입니다.");
        assertThat(Files.readString(file2)).isEqualTo("행정동 주소 파일 내용입니다.");
    }

}