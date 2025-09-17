package org.songeun.petdongne_server.address.presentation;

import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.application.AddressSearchRequestDto;
import org.songeun.petdongne_server.address.application.AddressSearchService;
import org.songeun.petdongne_server.address.infrastructure.dto.LegalAddressSearchQueryResponseDto;
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
            @Size(min = 1, max = 40, message = "검색어는 1자 이상 40자 이하여야 합니다.")
            String query,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "size는 1 이상이어야 합니다.")
            @Max(value = 100, message = "최대 요청 가능 수는 100입니다.")
            int size
    ) {
        Slice<LegalAddressSearchQueryResponseDto> searched = searchService.search(AddressSearchRequestDto.of(query, page, size));

        return ApiResponse.ok(AddressDtoConverter.convert(searched));
    }

}
