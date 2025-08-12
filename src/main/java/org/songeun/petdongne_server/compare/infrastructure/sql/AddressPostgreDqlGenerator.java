package org.songeun.petdongne_server.compare.infrastructure.sql;

import org.springframework.stereotype.Component;

@Component
public class AddressPostgreDqlGenerator implements AddressDqlSqlGenerator {

    @Override
    public String getExistsTableSqlTemplate() {
        return """
                    SELECT EXISTS (
                        SELECT 1
                        FROM information_schema.tables
                        WHERE table_schema = 'public'
                          AND table_name = ?
                    )
                """;
    }

    @Override
    public String getExistIndexSqlTemplate() {
        return """
                    SELECT COUNT(*) > 0 AS index_exists
                    FROM pg_indexes
                    WHERE tablename = ? AND indexname = ?
                """;
    }

}
