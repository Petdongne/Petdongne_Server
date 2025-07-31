package org.songeun.petdongne_server.address.infrastructure.crawling;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.songeun.petdongne_server.address.infrastructure.crawling.exception.AddressDataCrawlingException;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class AddressFilePatternTest {

    @Test
    @DisplayName("법정동, 행정동 주소 파일 패턴과 매칭되는 파일만 남긴다.")
    void shouldReturnMatchedFiles(){
        //given
        Path legalDongAddressFile = Path.of("KIKcd_B.20250714.xlsx");
        Path adminDongAddressFile = Path.of("KIKcd_H.20250714.xlsx");
        Path mergedFile = Path.of("KIKcd.20250714.xlsx");
        List<Path> files = List.of(legalDongAddressFile, adminDongAddressFile, mergedFile);

        //when
        Map<AddressFileType, Path> matchedFiles = AddressFilePattern.getMatchedFilesOrThrow(files);

        //then
        assertThat(matchedFiles)
                .hasSize(2)
                .containsEntry(AddressFileType.LEGAL_DONG_ADDRESS, legalDongAddressFile)
                .containsEntry(AddressFileType.ADMIN_DONG_ADDRESS, adminDongAddressFile);
    }
    
    @Test
    @DisplayName("법정동, 행정동 주소 파일이 하나라도 매칭되지 않으면 예외를 던진다.")
    void shouldThrowExceptionWhenAnyRequiredFileIsMissing(){
        //given
        Path legalDongAddressFile = Path.of("KIKcd_B.20250714.xlsx");
        Path orangeCatFile = Path.of("meow.xlsx");
        List<Path> files = List.of(legalDongAddressFile, orangeCatFile);

        //when & then
        assertThatThrownBy(() -> AddressFilePattern.getMatchedFilesOrThrow(files))
                .isInstanceOf(AddressDataCrawlingException.class);
    }

}