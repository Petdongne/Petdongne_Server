package org.songeun.petdongne_server.address.infrastructure.batch.legalDong;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.global.batch.policy.DeletedDataPolicy;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

// todo fieldName 분리, 공통 부모로 올리기(dateFormatter)
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
        LocalDate creationDate = parseLocalDateFormat(creationDateStr);

        // nullable 필드들
        String nullableSigungu = extractNullableField(rs, "시군구명");
        String nullableEupmyeondong = extractNullableField(rs, "읍면동명");
        String nullableRe = extractNullableField(rs, "동리명");
        String nullableDeletedDateStr = extractNullableField(rs, "말소일자");
        LocalDate nullableDeletedDate = nullableDeletedDateStr != null
                ? parseLocalDateFormat(nullableDeletedDateStr) : null;

        return LegalDongAddressRow.create(code, sido, nullableSigungu, nullableEupmyeondong,
                nullableRe, creationDate, nullableDeletedDate, deletedDataPolicy);
    }

    private String extractNullableField(RowSet rs, String fieldName) {
        String value = rs.getProperties().getProperty(fieldName);
        return StringUtils.hasText(value) ? value : null;
    }

    private LocalDate parseLocalDateFormat(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

}
