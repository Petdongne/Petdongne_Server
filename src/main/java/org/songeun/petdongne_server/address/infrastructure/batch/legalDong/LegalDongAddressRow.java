package org.songeun.petdongne_server.address.infrastructure.batch.legalDong;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.songeun.petdongne_server.global.batch.policy.DeletedDataPolicy;

import java.time.LocalDate;

@Builder
@ToString
@Getter
public class LegalDongAddressRow {

    private final String code;

    private final String sido;

    private final String sigungu;

    private final String eupmyeondong;

    private final String re;

    private final LocalDate creationDate;

    private final LocalDate deletedDate;

    private final DeletedDataPolicy deletedDataPolicy;

    public boolean isValid(LocalDate currentDate) {
        return deletedDataPolicy.isValidData(deletedDate, currentDate);
    }

}
