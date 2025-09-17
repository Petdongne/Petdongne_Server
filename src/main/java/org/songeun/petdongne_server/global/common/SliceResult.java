package org.songeun.petdongne_server.global.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SliceResult<T> {

    private final int currentPage;
    private final boolean hasPrevious;
    private final boolean hasNext;
    private final List<T> items;

    public static <T> SliceResult<T> from(Slice<T> slice) {
        return SliceResult.<T>builder()
                .currentPage(slice.getNumber())
                .hasPrevious(slice.hasPrevious())
                .hasNext(slice.hasNext())
                .items(slice.getContent())
                .build();
    }

}
