package org.songeun.petdongne_server.address.infrastructure.batch.adminDong;

import lombok.Builder;
import lombok.Getter;
import org.songeun.petdongne_server.global.batch.policy.DeletedDataPolicy;

import java.time.LocalDate;

@Builder
@Getter
public class AdminDongAddressRow {

    private final String code;

    private final String sido;

    private final String sigungu;

    private final String eupmyeondong;

    private final LocalDate creationDate;

    private final LocalDate deletedDate;

    private final DeletedDataPolicy deletedDataPolicy;

    public boolean isValidData(LocalDate currentDate) {
        return deletedDataPolicy.isDataValid(deletedDate, currentDate);
    }

}
