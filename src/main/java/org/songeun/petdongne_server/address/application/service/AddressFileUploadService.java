package org.songeun.petdongne_server.address.application.service;

import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.document.repository.AddressDocumentRepository;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.index.AddressIndexRepository;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.import.csv.record.AddressCsvRecord;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.import.csv.converter.AddressHierarchyConverter;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.import.csv.converter.AddressTypeConverter;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.import.csv.parser.CsvParser;
import org.songeun.petdongne_server.address.infrastructure.elasticsearch.import.csv.parser.ParserConfig;
import org.songeun.petdongne_server.address.presentation.dto.AddressImportRequestDto;
import org.songeun.petdongne_server.global.common.AllowedCharset;
import org.songeun.petdongne_server.global.file.ParserFactory;
import org.songeun.petdongne_server.global.file.validation.CharsetValidator;
import org.songeun.petdongne_server.global.file.validation.CsvHeaderValidator;
import org.songeun.petdongne_server.global.file.FileParseWithReader;
import org.songeun.petdongne_server.global.common.GlobalErrorStatus;
import org.songeun.petdongne_server.global.exception.SystemException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AddressDataImportService{

    private final AddressDocumentRepository documentRepository;
    private final AddressIndexRepository indexRepository;
    private final CharsetValidator charsetValidator;
    private final CsvHeaderValidator headerValidator;
    private final ParserFactory parserFactory;

    public void upload(AddressImportRequestDto requestDto)
            throws IOException, CsvRequiredFieldEmptyException
    {
        MultipartFile file = requestDto.file();
        AllowedCharset expectedCharset = requestDto.charset();

        charsetValidator.validateCharset(file, expectedCharset);
        Set<String> extractedHeaderNames = extractHeader(file, expectedCharset);
        headerValidator.validateHeader(AddressCsvRecord.class, extractedHeaderNames);

        // 인덱스 없으면 생성
        createIndexIfNeeded();

        // parse csv file to objects * 모든 파일 업로드 시 필수
        List<AddressCsvRecord> addresses = parseToAddressDocument(file, expectedCharset);
        // convert  to AddressDocument(아직 미작성)

        boolean succeed = documentRepository.bulkSave(addresses);
        if (!succeed){
            throw new SystemException(GlobalErrorStatus.CSV_FILE_READ_FAILED);
        }
    }

    private List<AddressCsvRecord> parseToAddressDocument(
            MultipartFile file,
            AllowedCharset charset
    ) {
        try {
            return FileParseWithReader.parse(
                    file,
                    charset.getIanaCharset(),
                    parserFactory.createParser(AddressCsvRecord.class)
            );

        } catch (IOException e) {
            throw new SystemException(GlobalErrorStatus.CSV_FILE_READ_FAILED, e); // todo
        }
    }

    private Set<String> extractHeader(
            MultipartFile file,
            AllowedCharset charset
    ) throws IOException {
        List<String> extractedHeader = FileParseWithReader.parse(
                file,
                charset.getIanaCharset(),
                parserFactory.createParser(String.class)
        );

        return toSet(extractedHeader);
    }

    private Set<String> toSet(List<String> strings) {
        return new HashSet<>(strings);
    }

    private void createIndexIfNeeded() {
        if (indexRepository.existIndex()){
            return;
        }

        if (indexRepository.createIndex()) {
            return;
        }

        throw new SystemException(GlobalErrorStatus.INTERNAL_SERVER_ERROR);
    }



}
