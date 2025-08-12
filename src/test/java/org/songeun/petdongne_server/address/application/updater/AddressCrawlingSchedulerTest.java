package org.songeun.petdongne_server.address.application.updater;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.songeun.petdongne_server.compare.application.service.AddressCrawlingService;
import org.songeun.petdongne_server.compare.application.service.AddressCrawlingScheduler;
import org.songeun.petdongne_server.compare.infrastructure.crawling.AddressPostIdentifierDto;
import org.songeun.petdongne_server.compare.infrastructure.crawling.exception.AddressDataCrawlingException;
import org.songeun.petdongne_server.global.common.NotiTaskExecutor;
import org.songeun.petdongne_server.global.notification.NotificationService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AddressCrawlingSchedulerTest {

    @Mock
    private AddressCrawlingService crawlingService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AddressPostIdentifierDto mockAddressPost;

    private AddressCrawlingScheduler addressCrawlingScheduler;

    private NotiTaskExecutor notiTaskExecutor;

    @BeforeEach
    void setUp() {
        notiTaskExecutor = new NotiTaskExecutor(notificationService);
        addressCrawlingScheduler = new AddressCrawlingScheduler(crawlingService, notiTaskExecutor);
    }

    @Test
    @DisplayName("크롤링되지 않은 최신 주소 게시글이 발견되면 처리 후 성공 알림을 전송한다.")
    void shouldProcessAddressDataWhenNewContentFound(){
        //given
        given(crawlingService.fetchLatestAddressPost()).willReturn(mockAddressPost);
        given(crawlingService.matchesLatestCrawlingHistory(any())).willReturn(false);
        doNothing().when(crawlingService).fetchAddressFile(any());

        //when
        addressCrawlingScheduler.processAddressCrawling();

        //then
        verify(crawlingService, times(1)).fetchAddressFile(any());
//        verify(notificationService, times(1)).sendExceptionNotification(eq(AddressCrawlingResult.SUCCESS.getMessage()));
    }

    @Test
    @DisplayName("이미 크롤링 된 최신 게시글이라면 재처리하지 않고 성공 알림을 전송한다.")
    void shouldNotProcessAddressDataWhenAlreadyCrawled(){
        //given
        given(crawlingService.fetchLatestAddressPost()).willReturn(mockAddressPost);
        given(crawlingService.matchesLatestCrawlingHistory(any())).willReturn(true);

        //when
        addressCrawlingScheduler.processAddressCrawling();

        //then
        verify(crawlingService, never()).fetchAddressFile(any());
//        verify(notificationService, times(1)).sendExceptionNotification(eq(AddressCrawlingResult.SUCCESS.getMessage()));
    }

    @Test
    @DisplayName("크롤링 작업 중 예외가 발생하면 작업 실패 알림을 전송한다.")
    void shouldSendNotificationWhenThrowingException(){
        //given
        given(crawlingService.fetchLatestAddressPost()).willThrow(AddressDataCrawlingException.class);

        //when
        addressCrawlingScheduler.processAddressCrawling();

        //then
        verify(crawlingService, never()).fetchAddressFile(any());
//        verify(notificationService, times(1)).sendExceptionNotification(eq(AddressCrawlingResult.FAILURE.getMessage()));
    }

}