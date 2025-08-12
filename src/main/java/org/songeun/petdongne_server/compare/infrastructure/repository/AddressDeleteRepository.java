package org.songeun.petdongne_server.compare.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.compare.infrastructure.schema.AddressSchemaManager;
import org.songeun.petdongne_server.compare.infrastructure.sql.AddressDmlSqlGenerator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AddressDeleteRepository {

    private final AddressDmlSqlGenerator dmlSqlGenerator;
    private final JdbcTemplate jdbcTemplate;
    private final AddressSchemaManager schemaManager;

    public void deleteAll() {
        String tableNameByView = schemaManager.getTableNameByView();
        if (tableNameByView == null) {
            throw new RuntimeException("Table targeted by view is not found");
        }
        String deleteAllSql = dmlSqlGenerator.getDeleteAllSql(tableNameByView);
        jdbcTemplate.execute(deleteAllSql);
    }


}
