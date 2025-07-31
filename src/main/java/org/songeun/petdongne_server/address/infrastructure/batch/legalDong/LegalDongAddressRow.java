package org.songeun.petdongne_server.address.infrastructure.batch.legalDong;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.songeun.petdongne_server.global.batch.policy.DeletedDataPolicy;

import java.time.LocalDate;

@Builder(access = AccessLevel.PRIVATE)
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

    public static LegalDongAddressRow create(
            String code,
            String sido,
            String sigungu,
            String eupmyeondong,
            String re,
            LocalDate creationDate,
            LocalDate deletedDate,
            DeletedDataPolicy deletedDataPolicy
    ){
        return LegalDongAddressRow.builder()
                .code(code)
                .sido((sido))
                .sigungu(sigungu)
                .eupmyeondong(eupmyeondong)
                .re(re)
                .creationDate(creationDate)
                .deletedDate(deletedDate)
                .deletedDataPolicy(deletedDataPolicy)
                .build();
    }

}
