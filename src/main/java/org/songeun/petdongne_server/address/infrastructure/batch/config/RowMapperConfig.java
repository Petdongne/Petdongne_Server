package org.songeun.petdongne_server.address.infrastructure.batch.config;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.batch.adminDong.AdminDongAddressRowMapper;
import org.songeun.petdongne_server.address.infrastructure.batch.legalDong.LegalDongAddressRowMapper;
import org.songeun.petdongne_server.global.batch.policy.DeletedDataPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RowMapperConfig {

    private final DeletedDataPolicy legacyDataNotUsedPolicy;

    @Bean
    public LegalDongAddressRowMapper legalDongAddressRowMapper() {
        return new LegalDongAddressRowMapper(legacyDataNotUsedPolicy);
    }

    @Bean
    public AdminDongAddressRowMapper adminDongAddressRowMapper() {
        return new AdminDongAddressRowMapper(legacyDataNotUsedPolicy);
    }

}
