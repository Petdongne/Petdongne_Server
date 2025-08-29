package org.songeun.petdongne_server.compare.infrastructure.sql;

import org.songeun.petdongne_server.compare.domain.entity.AddressTableMetaData;
import org.springframework.stereotype.Component;

@Component
public class AddressPostgreDdlGenerator implements AddressDdlSqlGenerator {

    @Override
    public String createTableSql(String tableName) {
        return String.format("""
                        CREATE TABLE IF NOT EXISTS %s (
                            %s SERIAL PRIMARY KEY,
                            %s VARCHAR(255),
                            %s VARCHAR(255) UNIQUE,
                            %s VARCHAR(255),
                            %s VARCHAR(255)
                        )
                        """,
                tableName,
                AddressTableMetaData.idColumnName(),
                AddressTableMetaData.codeColumnName(),
                AddressTableMetaData.fullAddressColumnName(),
                AddressTableMetaData.addressInitialsColumnName(),
                AddressTableMetaData.addressTypeColumnName()
        );
    }

    @Override
    public String createGinIndexSql(String indexName, String tableName, String columnName) {
        return String.format("""
                        CREATE INDEX %s
                        ON %s
                        USING gin (%s gin_trgm_ops)
                        """,
                indexName,
                tableName,
                columnName);
    }


    @Override
    public String createOrReplaceViewSql(String replaceTableName) {
        return String.format("""
                CREATE OR REPLACE VIEW %s AS SELECT * FROM %s;
                """,
                AddressTableMetaData.viewName(),
                replaceTableName
        );
    }

    @Override
    public String createGinExtensionSql() {
        return """
                CREATE EXTENSION IF NOT EXISTS pg_trgm;
                """;
    }

    @Override
    public String dropTableCascadeSql(String tableName) {
        return String.format("""
                        DROP TABLE IF EXISTS %s CASCADE
                        """,
                tableName);
    }

}
