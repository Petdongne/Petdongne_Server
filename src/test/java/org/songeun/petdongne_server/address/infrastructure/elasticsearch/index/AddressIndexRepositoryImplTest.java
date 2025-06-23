package org.songeun.petdongne_server.address.infrastructure.elasticsearch.index;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.addess.infrastructure.elasticsearch.index.AddressIndexRepositoryImpl;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AddressIndexRepositoryImplTest {

    @Mock
    ElasticsearchOperations integratedOperations;

    @Mock
    IndexOperations indexOperations;

    @InjectMocks
    AddressIndexRepositoryImpl repository;

    @Test
    @DisplayName("인덱스 생성에 성공하면 true를 반환한다.")
    void shouldReturnTrueWhenSuccess(){
        //given
        given(integratedOperations.indexOps(AddressDocument.class)).willReturn(indexOperations);
        given(indexOperations.createWithMapping())
                .willReturn(true);

        //when
        boolean result = repository.createIndex();

        //then
        assertTrue(result);
    }

    @Test
    @DisplayName("이미 존재하는 인덱스라면 true를 반환한다.")
    void shouldReturnTrueWhenExists(){
        //given
        given(integratedOperations.indexOps(AddressDocument.class)).willReturn(indexOperations);
        given(indexOperations.exists())
                .willReturn(true);

        //when
        boolean result = repository.existIndex();

        //then
        assertTrue(result);
    }

    @Test
    @DisplayName("존재하지 않는 인덱스라면 false를 반환한다.")
    void shouldReturnFalseWhenNotExists(){
        //given
        given(integratedOperations.indexOps(AddressDocument.class)).willReturn(indexOperations);
        given(indexOperations.exists())
                .willReturn(false);

        //when
        boolean result = repository.existIndex();

        //then
        assertFalse(result);
    }

}