package org.songeun.petdongne_server.compare.infrastructure.crawling.event;

import lombok.AllArgsConstructor;
import org.songeun.petdongne_server.compare.infrastructure.crawling.AddressFileType;

import java.nio.file.Path;
import java.util.Map;

@AllArgsConstructor
public class AddressSyncJobRequestDto {

    private final Map<AddressFileType, Path> filePaths;

    public static AddressSyncJobRequestDto of(Map<AddressFileType, Path> filePaths) {
        return new AddressSyncJobRequestDto( filePaths);
    }

    public Path getLegaldongFilePath() {
        return filePaths.get(AddressFileType.LEGAL_DONG_ADDRESS);
    }

    public Path getAdmindongFilePath() {
        return filePaths.get(AddressFileType.ADMIN_DONG_ADDRESS);
    }

}
