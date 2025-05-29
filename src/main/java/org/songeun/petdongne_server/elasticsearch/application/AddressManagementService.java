package org.songeun.petdongne_server.elasticsearch.application;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.elasticsearch.infrastructure.LegalDongAddressUploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AddressManagementService {

    private final LegalDongAddressUploader uploader;

    public void uploadFromCsv(MultipartFile file) {
        try {
            uploader.indexLegalDongAddressesFromCSV(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
