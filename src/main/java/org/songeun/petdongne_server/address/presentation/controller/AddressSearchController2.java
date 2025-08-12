package org.songeun.petdongne_server.address.presentation.controller;

import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.application.service.AddressOldSearchService;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.address.presentation.converter.AddressSearchConverter;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@Validated
public class AddressSearchController2 {

    private final AddressOldSearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam
            @NotBlank(message = "검색어는 공백일 수 없습니다.")
            @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]+$", message = "검색어는 한글, 영문, 숫자와 공백만 허용됩니다.")
            @Size(min = 1, max = 40, message = "검색어는 1자 이상 40자 이하여야 합니다.")
            String searchText,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "size는 1 이상이어야 합니다.")
            @Max(value = 100, message = "한 페이지당 최대 100개까지만 조회할 수 있습니다.")
            int size
    ) {
        Page<AddressDocument> response = searchService.search(searchText, PageRequest.of(page, size));

        return ApiResponse.ok(AddressSearchConverter.convert(response));
    }

}
