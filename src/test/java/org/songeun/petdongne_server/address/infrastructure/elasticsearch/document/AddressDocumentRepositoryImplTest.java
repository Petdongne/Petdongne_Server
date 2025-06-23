package org.songeun.petdongne_server.address.infrastructure.elasticsearch.document;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.repository.AddressDocumentRepositoryImpl;
import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.repository.ElasticsearchAddressRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AddressDocumentRepositoryImplTest {

    @Mock
    private ElasticsearchAddressRepository esAddressRepository;

    @InjectMocks
    private AddressDocumentRepositoryImpl addressDocumentRepository;

    @Test
    @DisplayName("모든 객체 저장에 성공하면 true를 반환한다.")
    void bulkSave(){
        //given
        int requestDocumentCount = 3;
        int responseDocumentCount = 3;
        List<AddressDocument> requestDocuments = createAddressDocuments(requestDocumentCount);
        List<AddressDocument> responseDocuments = createAddressDocuments(responseDocumentCount);

        given(esAddressRepository.saveAll(requestDocuments))
                .willReturn(responseDocuments);

        //when
        boolean result = addressDocumentRepository.bulkSave(requestDocuments);

        //then
        verify(esAddressRepository, Mockito.times(1)).saveAll(ArgumentMatchers.any());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("저장된 객체 수가 요청 수보다 적으면 false를 반환한다.")
    void bulkSave_fail(){
        //given
        int requestDocumentCount = 3;
        int responseDocumentCount = 2;
        List<AddressDocument> requestDocuments = createAddressDocuments(requestDocumentCount);
        List<AddressDocument> responseDocuments = createAddressDocuments(responseDocumentCount);

        given(esAddressRepository.saveAll(requestDocuments))
                .willReturn(responseDocuments);

        //when
        boolean result = addressDocumentRepository.bulkSave(requestDocuments);

        //then
        verify(esAddressRepository, Mockito.times(1)).saveAll(ArgumentMatchers.any());
        assertThat(result).isFalse();
    }

    private List<AddressDocument> createAddressDocuments(int count) {
        if (count <= 0) return Collections.emptyList();

        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> new AddressDocument())
                .toList();
    }

}