package org.songeun.petdongne_server.global.file;

import org.apache.commons.io.input.BOMInputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

public class FileParseWithReader {

    public static <T> List<T> parse(
            MultipartFile file,
            Charset charset,
            Parser<T> parser
    ) throws IOException {

        try (BOMInputStream bomInputStream = createBomInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(bomInputStream, charset))
        ) {
            return parser.parse(reader);
        }
    }
    
    /**
     * BOM(Byte Order Mark)이 포함되어 있을 수 있는 파일의 인코딩 문제를 방지하기 위해
     * BOMInputStream을 사용하여 스트림을 감쌉니다.
     * BOM이 감지되면 자동으로 스킵합니다.
     *
     * @param file BOM이 포함되어 있을 수 있는 입력 파일
     * @return BOM이 제거된 InputStream을 포함하는 BOMInputStream
     * @throws IOException 파일 스트림 처리 중 발생할 수 있는 예외
     */
    private static BOMInputStream createBomInputStream(MultipartFile file) throws IOException {
        return BOMInputStream.builder()
                .setInputStream(file.getInputStream())
                .get();
    }

}
