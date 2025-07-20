package org.songeun.petdongne_server.address.infrastructure.batch.compositeVer.mapper;

import org.songeun.petdongne_server.address.infrastructure.batch.compositeVer.readItem.AddressRow;
import org.songeun.petdongne_server.address.infrastructure.batch.compositeVer.readItem.AdmindongAddressRow;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AdminDongRowMapper implements RowMapper<AddressRow> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public AddressRow mapRow(RowSet rowSet) throws Exception {
        String deletedDateStr = rowSet.getProperties().getProperty("말소일자");

        return AdmindongAddressRow.builder()
                .code(rowSet.getProperties().getProperty("행정동코드"))
                .sido(rowSet.getProperties().getProperty("시도명"))
                .sigungu(rowSet.getProperties().getProperty("시군구명"))
                .eupmyeondong(rowSet.getProperties().getProperty("읍면동명"))
                .creationDate(LocalDate.parse(rowSet.getProperties().getProperty("생성일자"), DATE_FORMATTER))
                .deletedDate(
                        deletedDateStr != null && !deletedDateStr.isBlank()
                                ? Optional.of(LocalDate.parse(deletedDateStr, DATE_FORMATTER))
                                : Optional.empty())
                .build();
    }

}
