package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import lombok.*;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexNameFactory;
import org.songeun.petdongne_server.compare.domain.AddressParts;
import org.songeun.petdongne_server.compare.domain.entity.AddressType;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(
        indexName = AddressIndexNameFactory.ADDRESS_INDEX_ALIAS,
        createIndex = false,
        writeTypeHint = WriteTypeHint.FALSE
)
@Setting(settingPath = "/elasticsearch/address-setting.json")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class AddressDocument {

    @Id
    @Field(type = FieldType.Keyword, name = FieldConstants.CODE)
    private String code;

    @Field(type = FieldType.Text,
            name = FieldConstants.FULL_ADDRESS,
            analyzer = "address_ngram_analyzer")
    private String fullAddress;

    @Field(type = FieldType.Keyword, name = FieldConstants.TYPE)
    private AddressType type;

    public static AddressDocument create(
            String code, AddressParts addressParts
    ) {
        return AddressDocument.builder()
                .code(code)
                .fullAddress(addressParts.toFullAddress())
                .type(addressParts.toAddressType())
                .build();
    }

    public static class FieldConstants {
        public static final String ID = "id";
        public static final String CODE = "code";
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