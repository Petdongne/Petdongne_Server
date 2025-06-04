package org.songeun.petdongne_server.search.infrastructure;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.songeun.petdongne_server.search.domain.LegalDongAddress;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LegalDongAddressUploader {

    public static final Charset KOREAN_CSV_CHARSET = Charset.forName("CP949");
    public static final String LEGAL_DONG_ADDRESSES = "legal_dong_addresses";

    private final ElasticsearchOperations elasticsearchOperations;

    public List<IndexedObjectInformation> indexLegalDongAddressesFromCSV(InputStream csvInputStream) {

        if (elasticsearchOperations.indexOps(IndexCoordinates.of(LEGAL_DONG_ADDRESSES)).exists()) {
            elasticsearchOperations.indexOps(IndexCoordinates.of(LEGAL_DONG_ADDRESSES)).delete();
        }

        elasticsearchOperations.indexOps(LegalDongAddress.class).createWithMapping();

        try (Reader reader = new InputStreamReader(csvInputStream, KOREAN_CSV_CHARSET)) {
            // 쉼표로 구분된 CSV 형식으로 파싱
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(',')
                    .withTrim()
                    .parse(reader);

            List<IndexQuery> indexQueries = new ArrayList<>();

            for (CSVRecord record : records) {
                String sido = record.get("시도명");
                String sigungu = record.get("시군구명");
                String eupmyeondong = record.get("읍면동명");
                String re = record.get("리명");
                String fullAddress = record.get("주소");
                Integer level = Integer.parseInt(record.get("행정구역레벨"));
                String code = record.get("법정동코드");

                LegalDongAddress address = LegalDongAddress.builder()
                        .id(code)
                        .sido(sido)
                        .sigungu(sigungu)
                        .eupmyeondong(eupmyeondong)
                        .re(re)
                        .fullAddress(fullAddress)
                        .hierarchyLevel(level)
                        .build();

                IndexQuery indexQuery = new IndexQueryBuilder()
                        .withId(code)
                        .withObject(address)
                        .build();

                indexQueries.add(indexQuery);
            }

            return elasticsearchOperations.bulkIndex(indexQueries, IndexCoordinates.of(LEGAL_DONG_ADDRESSES));

        } catch (IOException e) {
            throw new RuntimeException("법정동 CSV 파싱 중 오류 발생", e);
        }
    }

}
