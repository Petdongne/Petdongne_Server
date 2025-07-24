package org.songeun.petdongne_server.global.batch.policy;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LegacyDataNotUsedPolicy implements DeletedDataPolicy {

    /**
     * 삭제일자와 현재일자를 비교하여 유효성을 판단합니다. <br/>
     * 현재일자 기준으로 이미 삭제된 데이터라면 유효하지 않은 데이터라고 판단합니다.
     * @param deletedDate 삭제일자
     * @param currentDate 현재일자
     * @return 삭제된 데이터라면 true를 반환합니다. 아니라면 false를 반환합니다.
     */
    @Override
    public boolean isDataValid(LocalDate deletedDate, LocalDate currentDate) {
        // 말소일자가 없으면 유효한 데이터
        if (deletedDate == null) {
            return true;
        }

        // 말소일자가 현재일자보다 이후면 아직 유효
        if (deletedDate.isAfter(currentDate)) {
            return true;
        }

        // 말소일자가 현재일자 이전이면 무효
        return false;
    }

}