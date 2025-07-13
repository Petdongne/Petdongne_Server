package org.songeun.petdongne_server.address.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.songeun.petdongne_server.global.file.validation.CsvFile;
import org.songeun.petdongne_server.global.common.AllowedCharset;
import org.springframework.web.multipart.MultipartFile;

public record AddressFileUploadRequestDto(

        @CsvFile
        @Schema(description = "주소 파일 (csv)", type = "string", format = "binary")
        MultipartFile file,

        @Schema(
                description = "파일 인코딩 방식",
                example = "UTF8",
                allowableValues = { "UTF8", "UTF8_BOM", "MS949", "EUC_KR" }
        )
        AllowedCharset charset
) {


}
