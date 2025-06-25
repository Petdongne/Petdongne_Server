package org.songeun.petdongne_server.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.songeun.petdongne_server.addess.presentation.dto.AddressSearchResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PagedResult<T> {

    private final int currentPage;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final int totalPages;
    private final long totalItems;
    private final List<T> items;

    public static <T> PagedResult<T> from(Page<T> page) {
        return PagedResult.<T>builder()
                .currentPage(page.getNumber())
                .hasPrevious(page.hasPrevious())
                .hasNext(page.hasNext())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .items(page.getContent())
                .build();
    }

}
