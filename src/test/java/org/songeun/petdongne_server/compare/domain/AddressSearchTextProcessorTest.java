package org.songeun.petdongne_server.compare.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AddressSearchTextProcessorTest {

    @Mock
    private RegionSynonymResolver regionResolver;

    @InjectMocks
    private AddressSearchTextProcessor addressSearchTextProcessor;

    @Test
    @DisplayName("주소 검색어로 활용 가능한 형태의 문자로 변환한다.")
    void shouldReturnAddressSearchText(){
        //given
        String text = "경기도 수원시 ^_^ !";
        given(regionResolver.resolve(ArgumentMatchers.any()))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        ProcessedSearchText processed = addressSearchTextProcessor.process(text);

        //then
        assertThat(processed).isNotNull();
        assertThat(processed.getContent()).containsExactly("경기도",  "수원시");
    }

}