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

import javax.annotation.Nullable;
import java.util.Set;

import static org.songeun.petdongne_server.address.infrastructure.elasticsearch.error.AddressErrorStatus.ALIAS_TARGET_NUM_INVALID;

@StepScope
@Component
@Slf4j
public class SwitchAddressAliasTargetTasklet implements Tasklet {

    private final IndexCoordinates newAddressIndex;
    private final AddressIndexOperations addressIndexRepository;

    public SwitchAddressAliasTargetTasklet(
            @Value("#{jobParameters['newIndexName']}")
            String newIndexName,
            AddressIndexOperations addressIndexRepository
    ) {
        this.newAddressIndex = IndexCoordinates.of(newIndexName);
        this.addressIndexRepository = addressIndexRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        IndexCoordinates legacyIndex = getCurrentIndexByAlias();
        makeAliasTargetToNewAddress();
        deleteLegacyIndexIfExists(legacyIndex);

        return RepeatStatus.FINISHED;
    }

    private void deleteLegacyIndexIfExists(IndexCoordinates indexCoords) {
        if (indexCoords != null) {
            addressIndexRepository.deleteIndex(indexCoords);
        }
    }

    private void makeAliasTargetToNewAddress() {
        addressIndexRepository.setAlias(newAddressIndex);
    }

    @Nullable
    private String findAliasTargetIndexName() {
        Set<String> indexNames = addressIndexRepository.findAliasTargetIndexNames();


        if (indexNames == null || indexNames.isEmpty()) {
            return null;
        }

        if (indexNames.size() > 1) {
            throw new BatchProcessingException(ALIAS_TARGET_NUM_INVALID);
        }

        return indexNames.iterator().next();
    }

    @Nullable
    private IndexCoordinates getCurrentIndexByAlias() {
        String name = findAliasTargetIndexName();
        return (name != null) ? IndexCoordinates.of(name) : null;
    }

}
