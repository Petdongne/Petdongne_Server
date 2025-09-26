package org.songeun.petdongne_server.address.infrastructure.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.address.domain.LegalAddress;
import org.songeun.petdongne_server.address.fixture.LegalAddressFixture;
import org.songeun.petdongne_server.testSupport.IntegrationTestSupport;
import org.songeun.petdongne_server.testSupport.PostgresSQLIntegrationTestSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LegalAddressCoreRepositoryTest extends PostgresSQLIntegrationTestSupport {

    @Autowired
    private LegalAddressCoreRepository legalAddressCoreRepository;

    @Test
    @DisplayName("주어진 모든 객체를 저장한다")
    void shouldSaveAll(){
        //given
        List<LegalAddress> legalAddresses = List.of(
                LegalAddressFixture.createLegalAddress("90909090", "강원특별자치도", "강", 35.5, 127.9),
                LegalAddressFixture.createLegalAddress("808080880", "강원특별자치도 춘천시", "강춘", 35.51, 127.91)
        );
        //when
        List<LegalAddress> saved = legalAddressCoreRepository.saveAll(legalAddresses);

        //then
        assertThat(saved.size()).isEqualTo(legalAddresses.size());
        assertThat(saved).extracting(LegalAddress::getFullAddress)
                .containsExactly(
                        "강원특별자치도",
                        "강원특별자치도 춘천시"
                );
    }

}