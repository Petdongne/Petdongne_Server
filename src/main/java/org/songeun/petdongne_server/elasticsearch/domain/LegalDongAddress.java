package org.songeun.petdongne_server.elasticsearch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "legal_dong_addresses")
@Setting(settingPath = "/elasticsearch/LegalDongAddressSetting.json")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LegalDongAddress {

    @Id
    @Field(type = FieldType.Keyword, analyzer = "korean")
    private String id;

    @Field(type = FieldType.Text, analyzer = "korean")
    private String sido;

    @Field(type = FieldType.Text, analyzer = "korean")
    private String sigungu;

    @Field(type = FieldType.Text, analyzer = "korean")
    private String eupmyeondong;

    @Field(type = FieldType.Text, analyzer = "korean")
    private String re;

    @Field(type = FieldType.Text, analyzer = "korean")
    private String fullAddress;

    @Field(type = FieldType.Integer)
    private Integer hierarchyLevel;

    @Field(type = FieldType.Long)
    private Long population;

}
