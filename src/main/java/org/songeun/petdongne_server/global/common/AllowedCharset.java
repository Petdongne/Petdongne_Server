package org.songeun.petdongne_server.global.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.nio.charset.Charset;

/**
 * 입력 받을 수 있는 파일 인코딩의 종류를 정의합니다.
 */
@Schema(description = "허용되는 Charset 목록")
@Getter
public enum AllowedCharset {

    @Schema(description = "UTF-8 (BOM 포함 가능)")
    UTF_8("UTF-8"),

    @Schema(description = "MS949")
    MS949("MS949"),

    @Schema(description = "EUC-KR")
    EUC_KR("EUC-KR")
    ;

    AllowedCharset(String name) {
        this.name = name;
        this.ianaCharset = Charset.forName(name);
    }

    private final String name;
    private final Charset ianaCharset;

}
