package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import lombok.*;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexNameFactory;
import org.songeun.petdongne_server.global.util.HashGenerator;
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
    @Field(type = FieldType.Keyword, name = FieldConstants.ID)
    private String id;

    @Field(type = FieldType.Keyword, name = FieldConstants.CODE)
    private String code;

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

    public static AddressDocument createAdminAddressDocument(
            String code,
            AdminDongAddressParts addressParts
    ) {
        AddressHierarchy hierarchy = AddressHierarchy.determine(addressParts);

        AddressType adminType = AddressType.ADMIN_DONG_ADDRESS;
        String fullAddress = adminType.createFullAddress(addressParts);

        String id = HashGenerator.generate(fullAddress);

        return AddressDocument.builder()
                .id(id)
                .code(code)
                .sido(addressParts.getSido())
                .sigungu(addressParts.getSigungu())
                .eupmyeondong(addressParts.getEupmyeondong())
                .re(null) // 행정동 - '리' 주소 지원 X
                .fullAddress(fullAddress)
                .hierarchyLevel(hierarchy)
                .type(adminType)
                .build();
    }

    public static AddressDocument createLegalAddressDocument(
            String code,
            LegalDongAddressParts addressParts
    ) {
        AddressHierarchy hierarchy = AddressHierarchy.determine(addressParts);

        AddressType legalType = AddressType.LEGAL_DONG_ADDRESS;
        String fullAddress = legalType.createFullAddress(addressParts);

        String id = HashGenerator.generate(fullAddress);

        return AddressDocument.builder()
                .id(id)
                .code(code)
                .sido(addressParts.getSido())
                .sigungu(addressParts.getSigungu())
                .eupmyeondong(addressParts.getEupmyeondong())
                .re(addressParts.getRe())
                .hierarchyLevel(hierarchy)
                .type(legalType)
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