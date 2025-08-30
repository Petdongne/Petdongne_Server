package org.songeun.petdongne_server.compare.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UpdateType {

    CREATED("Created"),
    DELETED("Deleted");

    private final String value;

    public static UpdateType fromDbData(String dbData) {
        return Arrays.stream(UpdateType.values())
                .filter(updateType -> updateType.value.equals(dbData))
                .findAny()
                .orElseThrow();
    }

    public String toDbData() {
        return value;
    }

}
