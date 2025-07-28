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
class AddressUpdateSchedulerTest {

    @Mock
    private AddressCrawlingService crawlingService;

    @InjectMocks
    private AddressUpdateScheduler addressUpdateScheduler;

    @Test
    @DisplayName("업데이트가 필요한 경우 주소 업데이트를 시작한다.")
    void shouldTriggerAddressUpdateWhenPostIsNew(){
        //given
        given(crawlingService.getLatestAddressContent()).willReturn(mock(AddressPostIdentifierDto.class));
        given(crawlingService.isAlreadyProcessed(any())).willReturn(false);
        doNothing().when(crawlingService).processAddressFile(any());

        //when
        addressUpdateScheduler.checkAndTriggerUpdate();

        //then
        verify(crawlingService, times(1)).processAddressFile(any());
    }

    @Test
    @DisplayName("업데이트가 필요하지 않으면 주소 업데이트를 시작하지 않는다.")
    void shouldNotTriggerAddressUpdateWhenPostAlreadyProcessed(){
        //given
        given(crawlingService.getLatestAddressContent()).willReturn(mock(AddressPostIdentifierDto.class));
        given(crawlingService.isAlreadyProcessed(any())).willReturn(true);

        //when
        addressUpdateScheduler.checkAndTriggerUpdate();

        //then
        verify(crawlingService, never()).processAddressFile(any());
    }

}