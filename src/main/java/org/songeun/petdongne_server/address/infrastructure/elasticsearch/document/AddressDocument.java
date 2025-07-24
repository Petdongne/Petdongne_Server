package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.*;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.converter.AddressTypeCsvConverter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(
        indexName = AddressIndexNameFactory.ADDRESS_INDEX_ALIAS,
        createIndex = false,
        aliases = @Alias(value = "kor-address"),
        writeTypeHint = WriteTypeHint.FALSE
)
@Setting(settingPath = "/elasticsearch/address-setting.json")
@Getter
@NoArgsConstructor
public class AddressDocument {

    @Id
    @CsvBindByName(column = "코드")
    @Field(type = FieldType.Keyword, name = FieldConstants.ID)
    private String id;

    @CsvBindByName(column = "시도명")
    @Field(type = FieldType.Keyword, name = FieldConstants.SIDO)
    private String sido;

    @CsvBindByName(column = "시군구명")
    @Field(type = FieldType.Keyword, name = FieldConstants.SIGUNGU)
    private String sigungu;

    @CsvBindByName(column = "읍면동명")
    @Field(type = FieldType.Keyword, name = FieldConstants.EUPMYEONDONG)
    private String eupmyeondong;

    @CsvBindByName(column = "리명")
    @Field(type = FieldType.Keyword, name = FieldConstants.RE)
    private String re;

    @CsvBindByName(column = "주소")
    @MultiField(
            mainField = @Field(
                    type = FieldType.Text,
                    name = FieldConstants.FULL_ADDRESS,
                    analyzer = "address_ngram_analyzer"),
            otherFields = {
                    @InnerField(
                            suffix = FieldConstants.FULL_ADDRESS_PER_CHAR_SUFFIX,
                            type = FieldType.Text,
                            analyzer = "address_per_char_analyzer"),
                    @InnerField(
                            suffix = FieldConstants.FULL_ADDRESS_FIRST_CHAR_SUFFIX,
                            type = FieldType.Text,
                            analyzer = "address_first_char_analyzer")
            })
    private String fullAddress;

    @CsvCustomBindByName(column = "계층", converter = AddressHierarchyCsvConverter.class)
    @Field(type = FieldType.Integer, name = FieldConstants.HIERARCHY_LEVEL)
    private AddressHierarchy hierarchyLevel;

    @CsvCustomBindByName(column = "유형", converter = AddressTypeCsvConverter.class)
    @Field(type = FieldType.Keyword, name = FieldConstants.TYPE)
    private AddressType type;

    public static class FieldConstants {
        public static final String ID = "id";
        public static final String SIDO = "sido";
        public static final String SIGUNGU = "sigungu";
        public static final String EUPMYEONDONG = "eupmyeondong";
        public static final String RE = "re";
        public static final String FULL_ADDRESS = "fullAddress";
        public static final String FULL_ADDRESS_PER_CHAR_SUFFIX = "per_char";
        public static final String FULL_ADDRESS_FIRST_CHAR_SUFFIX = "first_char";
        public static final String HIERARCHY_LEVEL = "hierarchyLevel";
        public static final String TYPE = "type";
        public static final String SCORE = "_score";

        /**
         * Elasticsearch 멀티 필드 중 first_char 필드의 전체 경로를 반환합니다.
         * 예: "fullAddress.first_char"
         */
        public static String getFullAddressFirstChar(){
            return FULL_ADDRESS + "." + FULL_ADDRESS_FIRST_CHAR_SUFFIX;
        }

        /**
         * Elasticsearch 멀티 필드 중 per_char 필드의 전체 경로를 반환합니다.
         * 예: "fullAddress.per_char"
         */
        public static String getFullAddressPerChar(){
            return FULL_ADDRESS + "." + FULL_ADDRESS_PER_CHAR_SUFFIX;
        }
    }

}