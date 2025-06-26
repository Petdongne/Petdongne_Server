package org.songeun.petdongne_server.address.infrastructure.elasticsearch.config;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.converter.AddressHierarchyToIntegerConverter;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.converter.AddressTypeToStringConverter;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.converter.IntegerToAddressHierarchyConverter;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.converter.StringToAddressTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import java.time.Duration;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchClientConfig extends ElasticsearchConfiguration {

    private final ElasticsearchClientProperties properties;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(properties.getHostAndPort())
                .withConnectTimeout(Duration.ofSeconds(properties.getConnectionTimeout()))
                .withSocketTimeout(Duration.ofSeconds(properties.getSocketTimeout()))
                .build();
    }

    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(List.of(
                new StringToAddressTypeConverter(),
                new AddressTypeToStringConverter(),
                new IntegerToAddressHierarchyConverter(),
                new AddressHierarchyToIntegerConverter()
        ));
    }

}
