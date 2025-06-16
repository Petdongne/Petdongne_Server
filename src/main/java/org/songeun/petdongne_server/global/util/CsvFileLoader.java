package org.songeun.petdongne_server.global.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Function;

public class CsvFileLoader {

    public static <T> List<T> loadFromFile(
            MultipartFile file,
            Charset charset,
            Function<BufferedReader, CsvToBeanBuilder<T>> builderConfigurer
    ) throws IOException {

        try (BOMInputStream bomInputStream = createBomInputStream(file);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(bomInputStream, charset))) {

            CsvToBean<T> csvToBean = builderConfigurer.apply(reader).build();
            return csvToBean.parse();
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

