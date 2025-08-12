package org.songeun.petdongne_server.compare.infrastructure.sql;

public interface AddressDmlSqlGenerator {

    String getInsertSqlTemplate(
            String tableName, String codeParamName, String fullAddressParamName,
            String addressInitialsParamName, String typeParamName
    );

    String getDoNothingUpsertSqlTemplate(
            String tableName, String idParamName, String fullAddressParamName,
            String addressInitialsParamName, String addressTypeParamName
    );

    String getDeleteAllSql(String tableName);

}
