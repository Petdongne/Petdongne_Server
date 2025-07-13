package org.songeun.petdongne_server.address.presentation.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.application.service.AddressFileUploadService;
import org.songeun.petdongne_server.address.presentation.dto.AddressFileUploadRequestDto;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/admin/addresses")
@RequiredArgsConstructor
@Validated // todo @Valid와의 차이
public class AddressDocumentController {

    private final AddressFileUploadService fileUploadService;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveFromAddressFile(
            @RequestPart("file")
            AddressFileUploadRequestDto request
    ) {
        // 위치 고민
//        if (file.isEmpty()) return ApiResponse.fail(GlobalErrorStatus.FILE_IS_EMPTY);

//        fileUploadService.upload3(request);
        return ApiResponse.ok("업로드에 성공했습니다.");
    }

}
