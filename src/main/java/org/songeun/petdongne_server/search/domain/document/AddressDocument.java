package org.songeun.petdongne_server.search.domain.document;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.*;
import org.songeun.petdongne_server.search.domain.converter.AddressHierarchyConverter;
import org.songeun.petdongne_server.search.domain.converter.AddressTypeConverter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(
        indexName = "kor-address-v1",
        createIndex = false,
        aliases = @Alias(value = "kor-address"),
        writeTypeHint = WriteTypeHint.FALSE
)
@Setting(settingPath = "/elasticsearch/address-setting.json")
@Getter
@NoArgsConstructor
public class Address {

    @Id
    @CsvBindByName(column = "코드")
    @Field(type = FieldType.Keyword, name = AddressDocumentFields.ID)
    private String id;

    @CsvBindByName(column = "시도명")
    @Field(type = FieldType.Keyword, name = AddressDocumentFields.SIDO)
    private String sido;

    @CsvBindByName(column = "시군구명")
    @Field(type = FieldType.Keyword, name = AddressDocumentFields.SIGUNGU)
    private String sigungu;

    @CsvBindByName(column = "읍면동명")
    @Field(type = FieldType.Keyword, name = AddressDocumentFields.EUPMYEONDONG)
    private String eupmyeondong;

    @CsvBindByName(column = "리명")
    @Field(type = FieldType.Keyword, name = AddressDocumentFields.RE)
    private String re;

    @CsvBindByName(column = "주소")
    @Field(type = FieldType.Text, analyzer = "address_search_analyzer")
    private String fullAddress;

    @CsvCustomBindByName(column = "계층", converter = AddressHierarchyConverter.class)
    @Field(type = FieldType.Integer, name = AddressDocumentFields.HIERARCHY_LEVEL)
    private AddressHierarchy hierarchyLevel;

    @CsvCustomBindByName(column = "유형", converter = AddressTypeConverter.class)
    @Field(type = FieldType.Keyword, name = AddressDocumentFields.TYPE)
    private AddressType type;

    public static class AddressDocumentFields {
        public static final String ID = "id";
        public static final String SIDO = "sido";
        public static final String SIGUNGU = "sigungu";
        public static final String EUPMYEONDONG = "eupmyeondong";
        public static final String RE = "re";
        public static final String FULL_ADDRESS = "fullAddress";
        private static final String FULL_ADDRESS_PER_CHAR_SUFFIX = "per_char";
        private static final String FULL_ADDRESS_FIRST_CHAR_SUFFIX = "first_char";
        public static final String HIERARCHY_LEVEL = "hierarchyLevel";
        public static final String TYPE = "type";
        public static final String SCORE = "_score";

        public static String getFullAddressFirstChar(){
            return FULL_ADDRESS + "." + FULL_ADDRESS_FIRST_CHAR_SUFFIX;
        }

        public static String getFullAddressPerChar(){
            return FULL_ADDRESS + "." + FULL_ADDRESS_PER_CHAR_SUFFIX;
        }
    }

}