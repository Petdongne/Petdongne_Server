package org.songeun.petdongne_server.addess.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.addess.application.AddressSearchService;
import org.songeun.petdongne_server.global.common.ApiResponse;
import org.songeun.petdongne_server.global.common.PagedResult;
import org.songeun.petdongne_server.addess.presentation.dto.AddressSearchResponseDto;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressSearchController {

    private final AddressSearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String searchText, Pageable pageable) {
        PagedResult<AddressSearchResponseDto> response = searchService.searchFor(searchText, pageable);

        return ApiResponse.ok(response);
    }

}
