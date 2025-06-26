package org.songeun.petdongne_server.address.infrastructure.elasticsearch.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("prod")
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "feature.validation.elasticsearch.enabled", havingValue = "true")
@Slf4j
public class ElasticsearchIndexValidator implements ApplicationRunner {

    private final ElasticsearchOperations operations;

    private final List<Class<?>> requiredIndices = List.of(
            AddressDocument.class
    );

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // TODO: 운영 환경 배포 전, 안정성을 위해 로깅 대신 예외를 발생시켜 기동을 실패시켜야 함
        requiredIndices.stream()
                .filter(idx -> !operations.indexOps(idx).exists())
                .forEach(notExistIdx ->
                        log.warn("Required index '{}' does not exist in Elasticsearch", notExistIdx.getSimpleName())
                );
    }

}