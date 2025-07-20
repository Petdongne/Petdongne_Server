package org.songeun.petdongne_server.address.infrastructure.batch.compositeVer.readItem;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Optional;

public class AdmindongAddressRow extends AddressRow implements AdmindongType{

    @Builder
    public AdmindongAddressRow(String code, String sido, String sigungu, String eupmyeondong, 
                              LocalDate creationDate, Optional<LocalDate> deletedDate) {
        super(code, sido, sigungu, eupmyeondong, creationDate, deletedDate);
    }

}
