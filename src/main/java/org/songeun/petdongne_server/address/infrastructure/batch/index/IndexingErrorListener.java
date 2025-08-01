package org.songeun.petdongne_server.address.infrastructure.batch.index;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexOperations;
import org.springframework.batch.core.annotation.AfterChunkError;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class IndexingErrorListener {

    private final AddressIndexOperations addressIndexRepository;

    @AfterChunkError
    public void afterChunkError(final ChunkContext chunkContext) {
        String newIndexName = (String) chunkContext.getStepContext().getJobParameters().get("newIndexName");
        IndexCoordinates rollbackTarget = IndexCoordinates.of(newIndexName);
        addressIndexRepository.deleteIndex(rollbackTarget);
    }

}
