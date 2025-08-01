package org.songeun.petdongne_server.address.infrastructure.batch.index;

import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.global.batch.BatchProcessingException;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexOperations;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.error.AddressErrorStatus.DUPLICATED_ADDRESS_INDEX_NAME;
import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.error.AddressErrorStatus.FAIL_CREATE_INDEX;

@StepScope
@Component
@Slf4j
public class CreateNewAddressIndexTasklet implements Tasklet {

    private final String newIndexName;
    private final AddressIndexOperations addressIndexRepository;

    public CreateNewAddressIndexTasklet(
            @Value("#{jobParameters['newIndexName']}")
            String newIndexName,
            AddressIndexOperations addressIndexRepository
    ) {
        this.newIndexName = newIndexName;
        this.addressIndexRepository = addressIndexRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        IndexCoordinates newIndexCoords = IndexCoordinates.of(newIndexName);
        if (addressIndexRepository.existIndex(newIndexCoords)) {
            log.error("인덱스 이름 중복 - {}는 중복된 이름입니다.", newIndexName);
            throw new BatchProcessingException(DUPLICATED_ADDRESS_INDEX_NAME);
        }

        boolean created = addressIndexRepository.createIndex(newIndexCoords);
        if (!created) {
            log.error("{} 인덱스 생성 실패", newIndexName);
            throw new BatchProcessingException(FAIL_CREATE_INDEX);
        }

        return RepeatStatus.FINISHED;
    }

}
