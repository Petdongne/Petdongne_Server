package org.songeun.petdongne_server.search.domain.document;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.*;
import org.songeun.petdongne_server.search.domain.converter.AddressHierarchyConverter;
import org.songeun.petdongne_server.search.domain.converter.AddressTypeConverter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "kor-address-v1", createIndex = false, aliases = @Alias(value = "kor-address"))
@Setting(settingPath = "/elasticsearch/address-setting.json")
@Mapping(mappingPath = "/elasticsearch/address-mapping.json")
@Getter
@NoArgsConstructor
public class Address {

    @Id
    @CsvBindByName(column = "코드")
    private String id;

    @CsvBindByName(column = "시도명")
    private String sido;

    @CsvBindByName(column = "시군구명")
    private String sigungu;

    @CsvBindByName(column = "읍면동명")
    private String eupmyeondong;

    @CsvBindByName(column = "리명")
    private String re;

    @CsvBindByName(column = "주소")
    private String fullAddress;

    @CsvCustomBindByName(column = "계층", converter = AddressHierarchyConverter.class)
    private AddressHierarchy hierarchyLevel;

    @CsvCustomBindByName(column = "유형", converter = AddressTypeConverter.class)
    private AddressType type;

}