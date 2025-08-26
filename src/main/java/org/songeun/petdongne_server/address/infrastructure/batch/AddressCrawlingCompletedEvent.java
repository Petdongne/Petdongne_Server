package org.songeun.petdongne_server.address.infrastructure.batch;

import org.songeun.petdongne_server.compare.infrastructure.crawling.AddressFileType;
import org.springframework.context.ApplicationEvent;

import java.nio.file.Path;
import java.util.Map;

public class AddressCrawlingCompletedEvent extends ApplicationEvent {

    private final Map<AddressFileType, Path> filePaths;

    public AddressCrawlingCompletedEvent(Object source, Map<AddressFileType, Path> filePaths) {
        super(source);
        this.filePaths = filePaths;
    }

    public Path getLegaldongFilePath() {
        return filePaths.get(AddressFileType.LEGAL_DONG_ADDRESS);
    }

    public Path getAdmindongFilePath() {
        return filePaths.get(AddressFileType.ADMIN_DONG_ADDRESS);
    }

}
