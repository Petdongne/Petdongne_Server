package org.songeun.petdongne_server.address.support;

import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.repository.AddressDocumentRepository;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexOperations;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.search.AddressSearchRepository;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

@Testcontainers
public abstract class ElasticsearchIntegrationTestSupport extends IntegrationTestSupport {

    public static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:7.17.10";

    @Container
    static final ElasticsearchContainer ELASTICSEARCH_CONTAINER =
            new ElasticsearchContainer(ELASTICSEARCH_IMAGE)
                    .withReuse(true)
                    .withStartupTimeout(Duration.ofMinutes(2));

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        String hostAndPort = ELASTICSEARCH_CONTAINER.getHost() + ":" + ELASTICSEARCH_CONTAINER.getMappedPort(9200);
        registry.add("app.elasticsearch.hostAndPort", () -> hostAndPort);
        registry.add("app.elasticsearch.connectionTimeout", () -> 5);
        registry.add("app.elasticsearch.socketTimeout", () -> 20);
    }

    @Autowired
    protected AddressSearchRepository searchRepository;

    @Autowired
    protected AddressDocumentRepository documentRepository;

    @Autowired
    protected AddressIndexOperations indexOperations;

}
