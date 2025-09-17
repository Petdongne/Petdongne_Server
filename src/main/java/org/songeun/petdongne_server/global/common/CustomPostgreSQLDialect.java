package org.songeun.petdongne_server.global.common;

import org.geolatte.geom.Point;
import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.query.sqm.function.FunctionKind;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.query.sqm.produce.function.PatternFunctionDescriptorBuilder;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.spi.TypeConfiguration;

public class CustomPostgreSQLDialect extends PostgreSQLDialect {

    public CustomPostgreSQLDialect() {super();}

    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);
        SqmFunctionRegistry functionRegistry = functionContributions.getFunctionRegistry();
        TypeConfiguration typeConfiguration = functionContributions.getTypeConfiguration();

        // similarity function
        new PatternFunctionDescriptorBuilder(
                functionRegistry,
                "similarity",
                FunctionKind.NORMAL,
                "similarity(?1, ?2)")
                .setExactArgumentCount(2)
                .setInvariantType(typeConfiguration.getBasicTypeForJavaType(Float.class))
                .register();

        // ST_X function
        new PatternFunctionDescriptorBuilder(
                functionRegistry,
                "st_x",
                FunctionKind.NORMAL,
                "ST_X(?1)")
                .setExactArgumentCount(1)
                .setInvariantType(typeConfiguration.getBasicTypeForJavaType(Double.class))
                .register();


        // ST_Y function
        new PatternFunctionDescriptorBuilder(
                functionRegistry,
                "st_y",
                FunctionKind.NORMAL,
                "ST_Y(?1)")
                .setExactArgumentCount(1)
                .setInvariantType(typeConfiguration.getBasicTypeForJavaType(Double.class))
                .register();
    }

}
