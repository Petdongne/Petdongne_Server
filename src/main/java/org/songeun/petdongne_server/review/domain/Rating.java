package org.songeun.petdongne_server.review.domain;

import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;

import java.util.Arrays;

public enum Rating {
    ZERO(0.0),
    HALF(0.5),
    ONE(1.0),
    ONE_HALF(1.5),
    TWO(2.0),
    TWO_HALF(2.5),
    THREE(3.0),
    THREE_HALF(3.5),
    FOUR(4.0),
    FOUR_HALF(4.5),
    FIVE(5.0);

    private final Double valueDub;
    private final String valueStr;

    Rating(Double value) {
        this.valueDub = value;
        this.valueStr = value.toString();
    }

    public Double getDoubleValue() {
        return valueDub;
    }

    public static Rating fromValue(Double value) {
        return Arrays.stream(values())
                .filter(r -> r.valueDub.equals(value))
                .findFirst()
                .orElseThrow(() -> new BusinessException(GlobalErrorStatus.BAD_REQUEST));
    }

    public static Rating fromValue(String value) {
        return Arrays.stream(values())
                .filter(r -> r.valueStr.equals(value))
                .findFirst()
                .orElseThrow(() -> new BusinessException(GlobalErrorStatus.BAD_REQUEST));
    }
}
