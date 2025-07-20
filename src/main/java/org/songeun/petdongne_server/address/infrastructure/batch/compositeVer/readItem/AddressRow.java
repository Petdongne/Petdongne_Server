package org.songeun.petdongne_server.address.infrastructure.batch.compositeVer.readItem;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Optional;

@Getter
public abstract class AddressRow {

    private final String code;

    private final String sido;

    private final String sigungu;

    private final String eupmyeondong;

    private final LocalDate creationDate;

    private final Optional<LocalDate> deletedDate;

    protected AddressRow(String code, String sido, String sigungu, String eupmyeondong,
                       LocalDate creationDate, Optional<LocalDate> deletedDate) {
        this.code = code;
        this.sido = sido;
        this.sigungu = sigungu;
        this.eupmyeondong = eupmyeondong;
        this.creationDate = creationDate;
        this.deletedDate = deletedDate;
    }

}
