package org.songeun.petdongne_server.address.support;

import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.repository.AddressDocumentRepository;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexRepository;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.search.AddressSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public abstract class ElasticsearchIntegrationTestSupport {

    @Container
    static ElasticsearchContainer elasticsearchContainer =
            new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.17.10")
                    .withReuse(true);

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        if (!elasticsearchContainer.isRunning()) {
            elasticsearchContainer.start();
        }
        String hostAndPort = elasticsearchContainer.getHost() + ":" + elasticsearchContainer.getMappedPort(9200);
        registry.add("app.elasticsearch.hostAndPort", () -> hostAndPort);
        registry.add("app.elasticsearch.connectionTimeout", () -> 5);
        registry.add("app.elasticsearch.socketTimeout", () -> 20);
    }

    @Autowired
    protected AddressSearchRepository searchRepository;

    @Autowired
    protected AddressDocumentRepository documentRepository;

    @Autowired
    protected AddressIndexRepository addressIndexRepository;

}
