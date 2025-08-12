package org.songeun.petdongne_server.compare.infrastructure.sql;

import org.hibernate.validator.internal.constraintvalidators.hv.CodePointLengthValidator;
import org.songeun.petdongne_server.compare.domain.AddressTableMetaData;
import org.springframework.stereotype.Component;

@Component
public class AddressPostgreDmlSqlGeneratorImpl implements AddressDmlSqlGenerator {

    @Override
    public String getInsertSqlTemplate(
            String tableName,String codeParamName, String fullAddressParamName,
            String addressInitialsParamName, String typeParamName
    ) {
        return String.format("""
                        INSERT INTO %s (%s, %s, %s, %s)
                        VALUES (:%s, :%s, :%s, :%s)
                        """,
                tableName,
                AddressTableMetaData.codeColumnName(),
                AddressTableMetaData.fullAddressColumnName(),
                AddressTableMetaData.addressInitialsColumnName(),
                AddressTableMetaData.addressTypeColumnName(),
                codeParamName,
                fullAddressParamName,
                addressInitialsParamName,
                typeParamName
        );
    }

    @Override
    public String getDoNothingUpsertSqlTemplate(
            String tableName, String codeParamName, String fullAddressParamName,
            String addressInitialsParamName, String addressTypeParamName
    ) {
        return String.format("""
                        INSERT INTO %s (%s, %s, %s, %s)
                        VALUES (:%s, :%s, :%s, :%s)
                        ON CONFLICT (%s) DO NOTHING;
                        """,
                tableName,
                AddressTableMetaData.codeColumnName(),
                AddressTableMetaData.fullAddressColumnName(),
                AddressTableMetaData.addressInitialsColumnName(),
                AddressTableMetaData.addressTypeColumnName(),
                codeParamName,
                fullAddressParamName,
                addressInitialsParamName,
                addressTypeParamName,
                AddressTableMetaData.fullAddressColumnName()
        );
    }

    @Override
    public String getDeleteAllSql(String tableName) {
        return String.format("""
                        DELETE FROM %s;
                """, tableName);
    }

}
