package org.songeun.petdongne_server.compare.infrastructure.schema;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.compare.domain.AddressTableMetaData;
import org.songeun.petdongne_server.compare.infrastructure.sql.AddressDdlSqlGenerator;
import org.songeun.petdongne_server.compare.infrastructure.sql.AddressDqlSqlGenerator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddressSchemaManager {

    private final AddressDqlSqlGenerator dqlSqlGenerator;
    private final AddressDdlSqlGenerator ddlSqlGenerator;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void createTableIfNotExist(String tableName) {
        String createTableSql = ddlSqlGenerator.createTableSql(tableName);
        jdbcTemplate.execute(createTableSql);
    }

    // todo postgreManager로 변경
    @Transactional
    public void createGinExtensionIfNotExist() {
        String createGinExtensionSql = ddlSqlGenerator.createGinExtensionSql();
        jdbcTemplate.execute(createGinExtensionSql);
    }

    @Transactional
    public void createGinIndex(String tableName, String indexName, String columnName) {
        String createGinIndexSql = ddlSqlGenerator.createGinIndexSql(indexName, tableName, columnName);
        jdbcTemplate.execute(createGinIndexSql);
    }

    @Transactional
    public void createOrReplaceView(String tableName) throws SQLException {
        String orReplaceViewSql = ddlSqlGenerator.createOrReplaceViewSql(tableName);
        jdbcTemplate.execute(orReplaceViewSql);
    }

    public boolean existTable(String tableName) {
        String existsTableSqlTemplate = dqlSqlGenerator.getExistsTableSqlTemplate();
        Boolean exist = jdbcTemplate.queryForObject(existsTableSqlTemplate, Boolean.class, tableName);
        Objects.requireNonNull(exist);

        return exist;
    }

    public boolean existGinIndex(String tableName, String indexName) {
        String existIndexSqlTemplate = dqlSqlGenerator.getExistIndexSqlTemplate();
        Boolean exist = jdbcTemplate.queryForObject(existIndexSqlTemplate, Boolean.class, tableName, indexName);
        Objects.requireNonNull(exist);

        return exist;
    }

    public String getTableNameByView() {
        String getViewTargetTableNameQuery = """
                SELECT substring(definition FROM 'FROM\\s+([a-zA-Z0-9_]+)') AS referenced_table
                FROM pg_views
                WHERE viewname = ?
                """;

        return jdbcTemplate.queryForObject(getViewTargetTableNameQuery, String.class, AddressTableMetaData.viewName());
    }

    public void dropTableCascadeIfExist() {
        String tableNameByView = getTableNameByView();
        String dropTableCascadeSql = ddlSqlGenerator.dropTableCascadeSql(tableNameByView);
        jdbcTemplate.execute(dropTableCascadeSql);
    }

}
