package org.songeun.petdongne_server.address.presentation.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.application.service.AddressDocumentService;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/admin/addresses")
@RequiredArgsConstructor
public class AddressDocumentController {

    private final AddressDocumentService addressDocumentService;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveFromAddressFile(
            @Schema(description = "주소 파일 (csv)", type = "string", format = "binary")
            @RequestPart("file") MultipartFile file
    ) {
        if (file.isEmpty()) return ApiResponse.fail(GlobalErrorStatus.FILE_IS_EMPTY);

        addressDocumentService.saveFromFile(file);
        return ApiResponse.ok("업로드에 성공했습니다.");
    }

}
