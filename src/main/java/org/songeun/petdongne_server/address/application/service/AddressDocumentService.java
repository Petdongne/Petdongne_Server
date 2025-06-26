package org.songeun.petdongne_server.address.application.service;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.AddressDocument;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.repository.AddressDocumentRepository;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexRepository;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.exception.BusinessException;
import org.songeun.petdongne_server.global.exception.SystemException;
import org.songeun.petdongne_server.global.util.OpenCsvParserUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.songeun.petdongne_server.global.common.GlobalErrorStatus.INVALID_FILE_FORMAT;
import static org.songeun.petdongne_server.global.common.GlobalErrorStatus.MAX_FILE_SIZE_EXCEEDED;

@Service
@RequiredArgsConstructor
public class AddressDocumentService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private final AddressDocumentRepository documentRepository;
    private final AddressIndexRepository indexRepository;

    public void saveFromFile(MultipartFile file) {
        validateFile(file);
        createIndexIfNeeded();

        List<AddressDocument> addresses = parseCsvFile(file);
        documentRepository.bulkSave(addresses);
    }

    // 아래 메서드 인덱스 서비스가 좋을지도?
    private void createIndexIfNeeded() {
        if (indexRepository.existIndex()){
            return;
        }

        if (indexRepository.createIndex()) {
            return;
        }

        throw new SystemException(GlobalErrorStatus.INTERNAL_SERVER_ERROR);
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(MAX_FILE_SIZE_EXCEEDED);
        }

        String contentType = file.getContentType();
        if (!"text/csv".equals(contentType) && !"application/csv".equals(contentType)) {
            throw new BusinessException(INVALID_FILE_FORMAT);
        }
    }

    private List<AddressDocument> parseCsvFile(MultipartFile file) {
        return OpenCsvParserUtil.parse(
                file,
                StandardCharsets.UTF_8,
                AddressDocument.class,
                new HeaderColumnNameMappingStrategy<>()
        );
    }

}