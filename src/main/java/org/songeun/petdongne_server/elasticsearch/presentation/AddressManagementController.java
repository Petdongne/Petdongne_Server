package org.songeun.petdongne_server.elasticsearch.presentation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.elasticsearch.application.AddressManagementService;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/admin/addresses")
@RequiredArgsConstructor
public class AddressManagementController {

    private final AddressManagementService managementService;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadLegalDongAddresses(
            @Schema(description = "법정동 주소 파일 (csv)", type = "string", format = "binary")
            @RequestPart("file") MultipartFile file
    ) {
        managementService.uploadFromCsv(file);
        return ApiResponse.ok("업로드에 성공했습니다. 현재 업로드 방식은 임시적인 방법으로, 추후 변경될 수 있습니다.");
    }

}
