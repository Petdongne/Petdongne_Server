package org.songeun.petdongne_server.map.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.songeun.petdongne_server.global.common.ErrorStatus;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum MapErrorStatus implements ErrorStatus {
    ZOOM_LEVEL_OUT_OF_BOUNDS(HttpStatus.BAD_REQUEST, "ZOOM_LEVEL_OUT_OF_BOUNDS", "가능한 줌 레벨 범위를 벗어났습니다. 줌 레벨은 1-14까지만 가능합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
