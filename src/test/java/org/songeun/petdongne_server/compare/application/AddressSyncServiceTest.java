package org.songeun.petdongne_server.compare.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.compare.application.service.AddressSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AddressSyncServiceTest {

    @Autowired
    private AddressSyncService addressSyncService;

    @Test
    @DisplayName("최신 주소 데이터를 DB에 업데이트 한다.")
    void shouldUpdate(){
        //given

        //when
        addressSyncService.synchronizeAddressData();

        //then

    }

}