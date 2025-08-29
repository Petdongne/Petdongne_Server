package org.songeun.petdongne_server.global.config;

import org.hibernate.boot.model.relational.Namespace;
import org.hibernate.boot.model.relational.Sequence;
import org.hibernate.mapping.Table;
import org.hibernate.tool.schema.spi.SchemaFilter;
import org.hibernate.tool.schema.spi.SchemaFilterProvider;
import org.songeun.petdongne_server.compare.domain.entity.AddressTableMetaData;

import static org.songeun.petdongne_server.global.config.HibernateCustomSchemaFilterProvider.AddressExcludeSchemaFilter.ADDRESS_EXCLUDE_SCHEMA_FILTER;

public class HibernateCustomSchemaFilterProvider implements SchemaFilterProvider {

    @Override
    public SchemaFilter getCreateFilter() {
        return ADDRESS_EXCLUDE_SCHEMA_FILTER;
    }

    @Override
    public SchemaFilter getDropFilter() {
        return ADDRESS_EXCLUDE_SCHEMA_FILTER;
    }

    @Override
    public SchemaFilter getTruncatorFilter() {
        return ADDRESS_EXCLUDE_SCHEMA_FILTER;
    }

    @Override
    public SchemaFilter getMigrateFilter() {
        return ADDRESS_EXCLUDE_SCHEMA_FILTER;
    }

    @Override
    public SchemaFilter getValidateFilter() {
        return ADDRESS_EXCLUDE_SCHEMA_FILTER;
    }

    protected static class AddressExcludeSchemaFilter implements SchemaFilter {

        public static final AddressExcludeSchemaFilter ADDRESS_EXCLUDE_SCHEMA_FILTER = new AddressExcludeSchemaFilter();

        private AddressExcludeSchemaFilter() {}

        @Override
        public boolean includeNamespace(Namespace namespace) {
            return true;
        }

        @Override
        public boolean includeTable(Table table) {
            String tableName = table.getName();
            return !tableName.equalsIgnoreCase(AddressTableMetaData.viewName());
        }

        @Override
        public boolean includeSequence(Sequence sequence) {
            return true;
        }

    }

}
