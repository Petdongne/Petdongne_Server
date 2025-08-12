package org.songeun.petdongne_server.compare.infrastructure.sql;

public interface AddressDdlSqlGenerator {

    String createTableSql(String tableName);

    String createGinIndexSql(String indexName, String tableName, String columnName);

    String createOrReplaceViewSql(String tableName);

    String createGinExtensionSql();

    String dropTableCascadeSql(String tableName);

}
