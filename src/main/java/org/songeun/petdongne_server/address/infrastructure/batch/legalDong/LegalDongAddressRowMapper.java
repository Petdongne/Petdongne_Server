package org.songeun.petdongne_server.address.infrastructure.batch.legalDong;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.global.batch.policy.DeletedDataPolicy;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RequiredArgsConstructor
public class LegalDongAddressRowMapper implements RowMapper<LegalDongAddressRow> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DeletedDataPolicy deletedDataPolicy;

    @Override
    public LegalDongAddressRow mapRow(RowSet rs) throws Exception {
        // 필수 필드들
        String code = rs.getProperties().getProperty("법정동코드");
        String sido = rs.getProperties().getProperty("시도명");
        String creationDateStr = rs.getProperties().getProperty("생성일자");

        // nullable 필드들
        String nullableSigungu = rs.getProperties().getProperty("시군구명");
        String nullableEupmyeondong = rs.getProperties().getProperty("읍면동명");
        String nullableRe = rs.getProperties().getProperty("동리명");
        String nullableDeletedDateStr = rs.getProperties().getProperty("말소일자");

        LocalDate creationDate = parseLocalDateFormat(creationDateStr);
        LocalDate nullableDeletedDate = parseLocalDateFormatOrNull(nullableDeletedDateStr);

        return LegalDongAddressRow.builder()
                .code(code)
                .sido((sido))
                .sigungu(nullableSigungu)
                .eupmyeondong(nullableEupmyeondong)
                .re(nullableRe)
                .creationDate(creationDate)
                .deletedDate(nullableDeletedDate)
                .deletedDataPolicy(deletedDataPolicy)
                .build();
    }

    private LocalDate parseLocalDateFormat(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    private LocalDate parseLocalDateFormatOrNull(String nullableDateStr) {
        return StringUtils.hasText(nullableDateStr)
                ? parseLocalDateFormat(nullableDateStr)
                : null;
    }

}
