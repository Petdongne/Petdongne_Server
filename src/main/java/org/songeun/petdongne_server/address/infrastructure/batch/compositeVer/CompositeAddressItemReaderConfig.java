package org.songeun.petdongne_server.address.infrastructure.batch.compositeVer;

import org.songeun.petdongne_server.address.infrastructure.batch.compositeVer.readItem.AddressRow;
import org.songeun.petdongne_server.address.infrastructure.batch.compositeVer.mapper.AdminDongRowMapper;
import org.songeun.petdongne_server.address.infrastructure.batch.compositeVer.mapper.LegalDongRowMapper;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.support.CompositeItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Path;
import java.util.List;

@Configuration
public class CompositeAddressItemReaderConfig {

    @Bean
    public CompositeItemReader<AddressRow> compositeAddressItemReader(){
        // 법정동 주소와 중복되는 행정동 주소 필터링을 위해 [법정동 주소 reader -> 행정동 주소 reader] 순서 필수
        final List<ItemStreamReader<? extends AddressRow>> poiItemReaders = List.of(
                legalDongAddressReader(null), // 법정동
                adminDongAddressReader(null) // 행정동
        );

        return new CompositeItemReader<>(poiItemReaders);
    }
    
    @Bean
    @StepScope
    public PoiItemReader<AddressRow> legalDongAddressReader(
            @Value("#{jobParameters['legalDongAddressFilePath']}") String filePath
    ){
        System.out.println("파라미터: " + filePath);
        PoiItemReader<AddressRow> poiItemReader = new PoiItemReader<>();
        poiItemReader.setResource(new FileSystemResource(Path.of(filePath)));
        poiItemReader.setLinesToSkip(1);
        poiItemReader.setRowMapper(legalDongRowMapper());

        return poiItemReader;
    }

    @Bean
    @StepScope
    public PoiItemReader<AddressRow> adminDongAddressReader(
            @Value("#{jobParameters['adminDongAddressFilePath']}") String filePath
    ){
        System.out.println("파라미터: " + filePath);
        PoiItemReader<AddressRow> poiItemReader = new PoiItemReader<>();
        poiItemReader.setResource(new FileSystemResource(Path.of(filePath)));
        poiItemReader.setLinesToSkip(1);
        poiItemReader.setRowMapper(adminDongRowMapper());

        return poiItemReader;
    }

    @Bean
    public LegalDongRowMapper legalDongRowMapper() {
        return new LegalDongRowMapper();
    }

    @Bean
    public AdminDongRowMapper adminDongRowMapper() {
        return new AdminDongRowMapper();
    }

}
