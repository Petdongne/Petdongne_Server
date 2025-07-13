package org.songeun.petdongne_server.address.infrastructure.elasticsearch.upload.csv.parser;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.upload.csv.row.AddressDocumentCsv;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AddressFileParseTest {

/*    @Test
    @DisplayName("인코딩이 다르면 워떤 오류가 나는지 확인한다.")
    void parse() throws IOException {
        //given
        String wrongEncodedString = "코드,시도명,시군구명,읍면동명,리명,주소,계층,유형\n" +
                "1100000000,서울특별시,,,, 서울특별시,1,법정동";
        byte[] ms949Bytes = wrongEncodedString.getBytes("MS949");

        MultipartFile wrongEncodingFile = new MockMultipartFile(
                "file",
                "address.csv",
                "text/csv",
                ms949Bytes // MS949로 인코딩된 데이터를 그대로 줌
        );

        //when
        List parsed = FileParseWithReader.parse(wrongEncodingFile, StandardCharsets.UTF_8, reader -> {
                    CsvToBean toBean = new CsvToBeanBuilder<AddressDocumentCsv>(reader)
                            .withType(AddressDocumentCsv.class)
                            .withMappingStrategy(createMappingStrategy(AddressDocumentCsv.class))
                            .withIgnoreLeadingWhiteSpace(true)
                            .withSeparator(',')
                            .withFieldAsNull(EMPTY_SEPARATORS)
                            .build();

                    return toBean.parse();
                }
        );

        parsed.forEach(System.out::println);


        //then

    }*/

    private HeaderColumnNameMappingStrategy createMappingStrategy(Class<AddressDocumentCsv> clazz) {
        var mappingStrategy = new HeaderColumnNameMappingStrategy();
        mappingStrategy.setType(clazz);

        return mappingStrategy;
    }

}