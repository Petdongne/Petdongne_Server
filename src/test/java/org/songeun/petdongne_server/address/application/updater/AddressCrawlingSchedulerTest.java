package org.songeun.petdongne_server.address.application.updater;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.songeun.petdongne_server.address.application.service.AddressCrawlingService;
import org.songeun.petdongne_server.address.infrastructure.crawling.AddressPostIdentifierDto;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AddressCrawlingSchedulerTest {

    @Mock
    private AddressCrawlingService crawlingService;

    @InjectMocks
    private AddressCrawlingScheduler addressCrawlingScheduler;

    @Test
    @DisplayName("크롤링되지 않은 최신 주소 게시글이 발견되면 처리한다.")
    void shouldProcessAddressDataWhenNewContentFound(){
        //given
        given(crawlingService.getLatestAddressContent()).willReturn(mock(AddressPostIdentifierDto.class));
        given(crawlingService.isAlreadyCrawled(any())).willReturn(false);
        doNothing().when(crawlingService).processAddressFile(any());

        //when
        addressCrawlingScheduler.crawlAddressDataIfRequired();

        //then
        verify(crawlingService, times(1)).processAddressFile(any());
    }

    @Test
    @DisplayName("이미 크롤링 된 최신 게시글이이라면 재처리하지 않는다.")
    void shouldNotProcessAddressDataWhenAlreadyCrawled(){
        //given
        given(crawlingService.getLatestAddressContent()).willReturn(mock(AddressPostIdentifierDto.class));
        given(crawlingService.isAlreadyCrawled(any())).willReturn(true);

        //when
        addressCrawlingScheduler.crawlAddressDataIfRequired();

        //then
        verify(crawlingService, never()).processAddressFile(any());
    }

}