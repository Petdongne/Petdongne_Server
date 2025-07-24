package org.songeun.petdongne_server.address.infrastructure.batch.legalDong;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.repository.AddressDocumentRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import java.util.List;

@StepScope
@Component
public class LegalDongAddressItemWriter implements ItemWriter<AddressDocument> {

    private final IndexCoordinates newAddressIndex;
    private final AddressDocumentRepository documentRepository;

    public LegalDongAddressItemWriter(
            @Value("#{jobParameters['newIndexName']}")
            String newAddressIndex,
            AddressDocumentRepository documentRepository
    ) {
        this.newAddressIndex = IndexCoordinates.of(newAddressIndex);
        this.documentRepository = documentRepository;
    }

    @Override
    public void write(Chunk<? extends AddressDocument> chunk) throws Exception {
        documentRepository.saveAll((List<AddressDocument>) chunk.getItems(), newAddressIndex);
    }

}
