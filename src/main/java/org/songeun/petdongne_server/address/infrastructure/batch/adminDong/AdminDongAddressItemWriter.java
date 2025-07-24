package org.songeun.petdongne_server.address.infrastructure.batch.adminDong;

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
public class AdminDongAddressItemWriter implements ItemWriter<AddressDocument> {

    private final IndexCoordinates newAddressIndex;
    private final AddressDocumentRepository documentRepository;

    public AdminDongAddressItemWriter(
            @Value("#{jobParameters['newIndexName']}")
            String newIndexName,
            AddressDocumentRepository documentRepository
    ) {
        this.newAddressIndex = IndexCoordinates.of(newIndexName);
        this.documentRepository = documentRepository;
    }

    @Override
    public void write(Chunk<? extends AddressDocument> chunk) throws Exception {
        documentRepository.saveAll((List<AddressDocument>) chunk.getItems(), newAddressIndex);
    }

}
