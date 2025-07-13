package org.songeun.petdongne_server.address.infrastructure.elasticsearch.upload.csv.row;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.*;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressHierarchy;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressType;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.upload.csv.converter.AddressHierarchyCsvConverter;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.upload.csv.converter.AddressTypeCsvConverter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressDocumentCsv {

    @CsvBindByName(column = AddressDocumentCsvHeader.ID)
    private String id;

    @CsvBindByName(column = AddressDocumentCsvHeader.SIDO)
    private String sido;

    @CsvBindByName(column = AddressDocumentCsvHeader.SIGUNGU)
    private String sigungu;

    @CsvBindByName(column = AddressDocumentCsvHeader.EUPMYEONDONG)
    private String eupmyeondong;

    @CsvBindByName(column = AddressDocumentCsvHeader.RE)
    private String re;

    @CsvBindByName(column = AddressDocumentCsvHeader.FULLADDRESS)
    private String fullAddress;

    @CsvCustomBindByName(column = AddressDocumentCsvHeader.HIERARCHY_LEVEL, converter = AddressHierarchyCsvConverter.class)
    private AddressHierarchy hierarchyLevel;

    @CsvCustomBindByName(column = AddressDocumentCsvHeader.TYPE, converter = AddressTypeCsvConverter.class)
    private AddressType type;

}
