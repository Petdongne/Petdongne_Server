package org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.repository;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.AddressDocument;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.StreamSupport;

@Repository
@RequiredArgsConstructor
public class AddressDocumentRepositoryImpl implements AddressDocumentRepository {

    private final ElasticsearchAddressRepository addressRepository;

    @Override
    public boolean bulkSave(List<AddressDocument> addresses) {
        Iterable<AddressDocument> saved = addressRepository.saveAll(addresses);
        long savedCount = StreamSupport.stream(saved.spliterator(), false).count();

        return savedCount == addresses.size();
    }

    @Override
    public void deleteAll(){
        addressRepository.deleteAll();
    }

}
