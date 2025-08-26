package org.songeun.petdongne_server.compare.presentation.controller;

import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.compare.application.service.AddressSearchService;
import org.songeun.petdongne_server.compare.domain.Address;
import org.songeun.petdongne_server.compare.application.dto.AddressSearchRequestDto;
import org.songeun.petdongne_server.compare.infrastructure.repository.AddressSearchResponse;
import org.songeun.petdongne_server.compare.presentation.AddressToDtoConverter;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@Validated
public class AddressSearchController {

    private final AddressSearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam
            @NotBlank(message = "검색어는 공백일 수 없습니다.")
            @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]+$", message = "검색어는 한글, 숫자와 공백만 허용됩니다.")
            @Size(min = 1, max = 40, message = "검색어는 1자 이상 40자 이하여야 합니다.")
            String query,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "size는 1 이상이어야 합니다.")
            @Max(value = 100, message = "한 번에 최대 100개의 주소만 조회할 수 있습니다.")
            int size
    ) {
        Slice<AddressSearchResponse> searched = searchService.search(AddressSearchRequestDto.of(query, page, size));

        return ApiResponse.ok(AddressToDtoConverter.convert(searched));
    }

}
