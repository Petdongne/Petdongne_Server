package org.songeun.petdongne_server.address.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.songeun.petdongne_server.address.infrastructure.crawling.AddressFilePattern;
import org.songeun.petdongne_server.address.infrastructure.crawling.AddressFileType;
import org.songeun.petdongne_server.address.infrastructure.crawling.AddressPostIdentifierDto;
import org.songeun.petdongne_server.address.infrastructure.crawling.event.AddressCrawlingCompletedEvent;
import org.songeun.petdongne_server.address.infrastructure.crawling.history.CrawledAddressPost;
import org.songeun.petdongne_server.address.infrastructure.crawling.AddressDataCrawler;
import org.songeun.petdongne_server.address.infrastructure.crawling.history.CrawledAddressPostRepository;
import org.songeun.petdongne_server.global.exception.SystemException;
import org.songeun.petdongne_server.global.util.ZipExtractor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.songeun.petdongne_server.global.common.GlobalErrorStatus.COLLECT_CHILD_FILES_FAILED;
import static org.songeun.petdongne_server.global.common.GlobalErrorStatus.UNZIP_PROCESS_FAILED;

/**
 * 크롤링 해 온 파일 중 원하는 파일명을 가진 파일 경로만 매핑. 이때 원하는 파일은 법정동과 행정동이다.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AddressCrawlingService {
    
    private final AddressDataCrawler addressDataCrawler;
    private final ApplicationEventPublisher publisher;
    private final CrawledAddressPostRepository crawledAddressPostRepository;

    /**
     * 주어진 주소 데이터 식별자를 사용하여 주소 파일을 다운로드 받은 뒤,
     * 법정동 및 행정동 주소 파일만 선별하여 이벤트로 발행합니다.
     *
     * @param addressIdentifier 주소 데이터 식별자
     */
    public void processAddressFile(AddressPostIdentifierDto addressIdentifier) {
        Path newAddressZipFile = addressDataCrawler.downloadAddressZipFile(addressIdentifier);
        List<Path> addressFiles = unzipFile(newAddressZipFile);
        Map<AddressFileType, Path> matchedFiles = AddressFilePattern.getMatchedFilesOrThrow(addressFiles);
        crawledAddressPostRepository.save(CrawledAddressPost.of(addressIdentifier.nttId()));

        publisher.publishEvent(new AddressCrawlingCompletedEvent(this, matchedFiles));
    }

    private List<Path> unzipFile(Path zipFilePath) {
        Path outputDir = makeOutputDirFrom(zipFilePath);
        unzip(zipFilePath, outputDir);

        return getInnerFiles(outputDir);
    }

    private List<Path> getInnerFiles(Path sourcePath) {
        try (Stream<Path> files = Files.list(sourcePath)){
            return files.filter(Files::isRegularFile).collect(Collectors.toList());

        } catch (IOException e) {
            log.error("하위 파일 수집 중 오류 발생 - 경로: {}, 원인: {}", sourcePath, e.getMessage());
            throw new SystemException(COLLECT_CHILD_FILES_FAILED);
        }
    }

    private void unzip(Path zipFilePath, Path outputDir) {
        try {
            ZipExtractor.unzip(zipFilePath, outputDir);
        } catch (IOException e) {
            log.error("주소 파일 압축 해제 중 오류 발생: {}", e.getMessage());
            throw new SystemException(UNZIP_PROCESS_FAILED);
        }
    }

    /**
     * 주어진 파일 경로를 기반으로 파일명의 .zip 확장자를 제거한 경로를 생성합니다.
     * @param zipFilePath .zip 파일의 전체 경로
     * @return .zip 확장자가 제거된 디렉토리 경로
     */
    private Path makeOutputDirFrom(Path zipFilePath) {
        Path parent = zipFilePath.getParent();

        String fileNameWithExtension = zipFilePath.getFileName().toString();
        String fileNameWithoutExtension = fileNameWithExtension.replaceFirst("\\.zip$", "");

        return (parent != null) ? parent.resolve(fileNameWithoutExtension) : Path.of(fileNameWithoutExtension);
    }

    public AddressPostIdentifierDto getLatestAddressContent() {
        return addressDataCrawler.fetchLatestPostIdentifier();
    }

    public boolean isAlreadyCrawled(AddressPostIdentifierDto latestFoundPost) {
        Optional<CrawledAddressPost> processed = crawledAddressPostRepository.findTopByOrderByNttIdDesc();

        // 처리된 이력이 없음
        if (processed.isEmpty()) {
            return false;
        }

        // 새로운 데이터임
        if (!processed.get().matches(latestFoundPost.nttId())) {
            return false;
        }

        return true;
    }

}
