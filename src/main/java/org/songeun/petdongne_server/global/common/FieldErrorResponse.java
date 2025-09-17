package org.songeun.petdongne_server.global.common;

public record FieldErrorResponse(
        String field,
        String message
) {
}
