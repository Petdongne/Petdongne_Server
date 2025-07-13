package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import lombok.*;
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
public class AddressDocument {

    @Id
    @Field(type = FieldType.Keyword, name = FieldConstants.ID)
    private String id;

    @Field(type = FieldType.Keyword, name = FieldConstants.SIDO)
    private String sido;

    @Field(type = FieldType.Keyword, name = FieldConstants.SIGUNGU)
    private String sigungu;

    @Field(type = FieldType.Keyword, name = FieldConstants.EUPMYEONDONG)
    private String eupmyeondong;

    @Field(type = FieldType.Keyword, name = FieldConstants.RE)
    private String re;

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

    @Field(type = FieldType.Integer, name = FieldConstants.HIERARCHY_LEVEL)
    private AddressHierarchy hierarchyLevel;

    @Field(type = FieldType.Keyword, name = FieldConstants.TYPE)
    private AddressType type;


    // todo 개별 클래스로 분리
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