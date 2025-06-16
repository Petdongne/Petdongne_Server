package org.songeun.petdongne_server.global.util;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.MappingStrategy;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.exception.SystemException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import static com.opencsv.enums.CSVReaderNullFieldIndicator.EMPTY_SEPARATORS;

public class CsvParser {

    private static final char CSV_SEPARATOR = ',';
    private static final boolean IGNORE_WHITESPACE = true;

    /**
     * 구분자가 ','인 CSV 파일을 지정된 타입의 객체 리스트로 파싱합니다.
     *
     * @param file    (input) CSV 파일
     * @param charset 파일 인코딩
     * @param clazz   (output) 객체 클래스
     * @param <T>     객체 타입
     * @param mappingStrategy CSV 파일 매핑 전략
     * @return 변환된 객체 리스트
     * @throws SystemException CSV 처리 중 오류가 발생한 경우
     */
    public static <T> List<T> parse(
            MultipartFile file,
            Charset charset,
            Class<T> clazz,
            MappingStrategy<T> mappingStrategy
    ) {
        mappingStrategy.setType(clazz);

        try {
            return CsvFileLoader.loadFromFile(file, charset, reader ->
                    new CsvToBeanBuilder<T>(reader)
                            .withType(clazz)
                            .withMappingStrategy(mappingStrategy)
                            .withIgnoreLeadingWhiteSpace(IGNORE_WHITESPACE)
                            .withSeparator(CSV_SEPARATOR)
                            .withFieldAsNull(EMPTY_SEPARATORS)
            );

        } catch (IOException e) {
            throw new SystemException(GlobalErrorStatus.CSV_FILE_READ_FAILED, e);
        }

    }

}



