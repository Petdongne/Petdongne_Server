package org.songeun.petdongne_server.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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

}
