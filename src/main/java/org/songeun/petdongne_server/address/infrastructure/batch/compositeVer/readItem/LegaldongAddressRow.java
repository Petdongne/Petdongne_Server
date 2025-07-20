package org.songeun.petdongne_server.address.infrastructure.batch.compositeVer.readItem;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Optional;

@Getter
public class LegaldongAddressRow extends AddressRow implements LegaldongType{

    private final Optional<String> re;

    @Builder
    public LegaldongAddressRow(String code, String sido, String sigungu, String eupmyeondong, 
                              LocalDate creationDate, Optional<LocalDate> deletedDate, Optional<String> re) {
        super(code, sido, sigungu, eupmyeondong, creationDate, deletedDate);
        this.re = re;
    }

}
