package org.songeun.petdongne_server.compare.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.compare.domain.Address;
import org.songeun.petdongne_server.compare.infrastructure.schema.AddressSchemaManager;
import org.songeun.petdongne_server.compare.infrastructure.sql.AddressDmlSqlGenerator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.songeun.petdongne_server.compare.domain.AddressTableMetaData.*;

@Repository
@RequiredArgsConstructor
public class AddressInsertRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AddressDmlSqlGenerator dmlSqlGenerator;
    private final AddressSchemaManager addressSchemaManager;

    public void batchInsert(List<Address> addresses) {
        String tableName = addressSchemaManager.getTableNameByView();

        String insertSqlTemplate = dmlSqlGenerator.getInsertSqlTemplate(tableName, codeColumnName(), fullAddressColumnName(),
                addressInitialsColumnName(), addressTypeColumnName());

        SqlParameterSource[] batchParams = addresses.stream()
                .map(address -> new MapSqlParameterSource()
                        .addValue(codeColumnName(), address.getCode())
                        .addValue(fullAddressColumnName(), address.getFullAddress())
                        .addValue(addressInitialsColumnName(), address.getAddressInitials())
                        .addValue(addressTypeColumnName(), address.getType().getKoreanName())
                )
                .toArray(SqlParameterSource[]::new);

        int[] updated = jdbcTemplate.batchUpdate(insertSqlTemplate, batchParams);
        if (updated.length != addresses.size()) {
            throw new RuntimeException("Insert failed");
        }
    }

}
