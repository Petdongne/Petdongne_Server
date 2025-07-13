package org.songeun.petdongne_server.global.file.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CsvHeaderValidator {

    private final List<ComparableCsvHeader> comparableHeaders;

    public void validateHeader(Class clazz, Set<String> headerNames) {
        ComparableCsvHeader comparableHeader = select(clazz);

        if (!comparableHeader.compare(headerNames)) {
            throw new RuntimeException();
        }
    }

    private ComparableCsvHeader select(Class clazz) {
        for (ComparableCsvHeader header : comparableHeaders) {
            boolean supported = header.isSupported(clazz);
            if (supported) {
                return header;
            }
        }
        throw new IllegalArgumentException("No header validator found for class: " + clazz.getName());
    }

}
/*    public void validateHeader(
            MultipartFile file,
            AllowedCharset charset,
            HeaderComparable csvDto
    ) throws CsvRequiredFieldEmptyException, IOException {
        // 헤더 검증* 이것도 여러 군데서 필요하므로 분리가 필요할 것 같다.
        var mappingStrategy = new HeaderColumnNameMappingStrategy<>();
        String[] definedHeader = mappingStrategy.generateHeader(csvDto);

        // read header of multipart file
        // FileParseWithReader 사용 -> 전략 객체 사용할 것인지 ? -> 테스트 용이성을 위해 분리하자
        List<String> extractedHeader = FileParseWithReader.parse(
                file,
                charset.getIanaCharset(),
                reader -> {
                    try (CSVReader csvReader = new CSVReader(reader)) {
                        return List.of(csvReader.readNext());
                    } catch (IOException | CsvValidationException e) {
                        throw new RuntimeException(e);
                    }
                });

        HashSet<String> definedHeaderSet = new HashSet<>(List.of(definedHeader));
        definedHeaderSet.forEach(extractedHeader::remove);
        if (!definedHeaderSet.isEmpty()) {
            throw new RuntimeException("주어진 파일의 헤더가 기존 정의된 헤더와 일치하지 않습니다.");
        }
    }*/



//}
