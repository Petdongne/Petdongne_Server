package org.songeun.petdongne_server.address.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.songeun.petdongne_server.compare.application.service.AddressCrawlingService;
import org.songeun.petdongne_server.compare.infrastructure.crawling.AddressDataCrawler;
import org.songeun.petdongne_server.compare.infrastructure.crawling.AddressPostIdentifierDto;
import org.songeun.petdongne_server.compare.infrastructure.crawling.event.AddressSyncJobRequestDto;
import org.songeun.petdongne_server.compare.infrastructure.crawling.exception.AddressDataCrawlingException;
import org.songeun.petdongne_server.compare.infrastructure.crawling.history.CrawledAddressPost;
import org.songeun.petdongne_server.compare.infrastructure.crawling.history.CrawledAddressPostRepository;
import org.songeun.petdongne_server.testSupport.FileUtils;
import org.springframework.context.ApplicationEventPublisher;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressCrawlingServiceTest {

    @Mock
    private AddressDataCrawler addressDataCrawler;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private CrawledAddressPostRepository crawledAddressPostRepository;

    @InjectMocks
    private AddressCrawlingService addressCrawlingService;

    @TempDir
    private Path tempDir;

    @Test
    @DisplayName("가장 최근에 크롤링했던 게시글의 nttId와 동일하면 true를 반환한다.")
    void shouldReturnTrueWhenLatestCrawledPostMatches() {
        // given
        long latestCrawledNttId = 3L;
        CrawledAddressPost latestPost = CrawledAddressPost.of(latestCrawledNttId);

        given(crawledAddressPostRepository.findTopByOrderByNttIdDesc())
                .willReturn(Optional.of(latestPost));

        var post = createPostWith(latestCrawledNttId);

        // when
        boolean result = addressCrawlingService.matchesLatestCrawlingHistory(post);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("가장 최근에 크롤링했던 게시글의 nttId와 같지 않으면 false를 반환한다.")
    void shouldReturnFalseWhenPostIsNewerThanLatestCrawled() {
        // given
        long latestCrawledNttId = 2L;
        CrawledAddressPost latestPost = CrawledAddressPost.of(latestCrawledNttId);

        given(crawledAddressPostRepository.findTopByOrderByNttIdDesc())
                .willReturn(Optional.of(latestPost));

        long newerNttId = 3L;
        var post = createPostWith(newerNttId);

        // when
        boolean result = addressCrawlingService.matchesLatestCrawlingHistory(post);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("크롤링했던 이력이 없다면 false를 반환한다.")
    void shouldReturnFalseWhenNoPreviousCrawlingHistory() {
        // given
        given(crawledAddressPostRepository.findTopByOrderByNttIdDesc())
                .willReturn(Optional.empty());

        long nttId = 1L;
        var post = createPostWith(nttId);

        // when
        boolean result = addressCrawlingService.matchesLatestCrawlingHistory(post);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("정상적인 주소 데이터가 다운로드되면 이벤트를 발행한다.")
    void shouldPublishEventWhenCompletedDownload() throws IOException {
        // given
        AddressPostIdentifierDto postDto = createPostWith(1L);

        Path legalDongAddressFile = Path.of("KIKcd_B.20250714.xlsx");
        Path adminDongAddressFile = Path.of("KIKcd_H.20250714.xlsx");
        Path downloadedPath = setUpZipFile(legalDongAddressFile, adminDongAddressFile);
        given(addressDataCrawler.downloadAddressZipFile(postDto))
                .willReturn(downloadedPath);

        // when
        addressCrawlingService.fetchAddressFile(postDto);

        // then
        verify(crawledAddressPostRepository, times(1))
                .save(any(CrawledAddressPost.class));

        assertCrawlingCompletedEventPublished(legalDongAddressFile, adminDongAddressFile);
    }

    private void assertCrawlingCompletedEventPublished(Path legalDongAddressFile, Path adminDongAddressFile) {
        ArgumentCaptor<AddressSyncJobRequestDto> captor = ArgumentCaptor.forClass(AddressSyncJobRequestDto.class);
        verify(eventPublisher, times(1))
                .publishEvent(captor.capture());

        AddressSyncJobRequestDto event = captor.getValue();
        assertThat(event).isNotNull();
        assertThat(event.getLegaldongFilePath().getFileName()).isEqualTo(legalDongAddressFile.getFileName());
        assertThat(event.getAdmindongFilePath().getFileName()).isEqualTo(adminDongAddressFile.getFileName());
    }

    @Test
    @DisplayName("다운로드에 실패한 경우 크롤링 프로세스를 중단한다.")
    void shouldSuspendProcessWhenDownloadFails() {
        // given
        given(addressDataCrawler.downloadAddressZipFile(any(AddressPostIdentifierDto.class)))
                .willThrow(new AddressDataCrawlingException("다운로드 실패"));

        // when & then
        assertThatThrownBy(() -> addressCrawlingService.fetchAddressFile(createPostWith(1L)))
                .isInstanceOf(AddressDataCrawlingException.class)
                .hasMessage("다운로드 실패");

        verify(eventPublisher, never()).publishEvent(any());
        verify(crawledAddressPostRepository, never()).save(any());
    }

    @Test
    @DisplayName("다운로드 받은 파일 중 법정동/행정동 주소 파일이 없으면 크롤링 프로세스를 중단한다.")
    void shouldSuspendProcessWhenUnmatchedFiles() throws IOException {
        // given
        AddressPostIdentifierDto postDto = createPostWith(1L);

        Path downloadedPath = setUpInvalidZipFile();
        given(addressDataCrawler.downloadAddressZipFile(any(AddressPostIdentifierDto.class)))
                .willReturn(downloadedPath);

        // when & then
        assertThatThrownBy(() -> addressCrawlingService.fetchAddressFile(postDto))
                .isInstanceOf(AddressDataCrawlingException.class);

        // 실패 시 이벤트 발행하지 않음
        verify(eventPublisher, never()).publishEvent(any());
        verify(crawledAddressPostRepository, never()).save(any());
    }

    private AddressPostIdentifierDto createPostWith(long nttId) {
        return AddressPostIdentifierDto.of("bbsId", nttId);
    }

    private Path createZipFile(Path legalDongAddressFile, Path adminDongAddressFile) throws IOException {
        Map<Path, String> downloadData = new LinkedHashMap<>();
        downloadData.put(legalDongAddressFile, "법정동 주소 파일 내용입니다.");
        downloadData.put(adminDongAddressFile, "행정동 주소 파일 내용입니다.");

        Path outputPath = tempDir.resolve("addresses.zip");
        FileUtils.makeZip(downloadData, outputPath);

        return outputPath;
    }

    private Path setUpZipFile(Path legalDongAddressFile, Path adminDongAddressFile) throws IOException {
        return createZipFile(legalDongAddressFile, adminDongAddressFile);
    }

    private Path setUpInvalidZipFile() throws IOException {
        return createZipFile(Path.of("jajaja.xlsx"), Path.of("arrarr.xlsx"));
    }

}