package org.songeun.petdongne_server.address.infrastructure.crawling.event;

import org.songeun.petdongne_server.address.infrastructure.crawling.AddressFileType;
import org.springframework.context.ApplicationEvent;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * 주소 (법정동/행정동) 파일 다운로드 완료를 알리는 이벤트입니다.
 */
public class AddressCrawlingCompletedEvent extends ApplicationEvent {

    private final Map<AddressFileType, Path> filePaths;

    public AddressCrawlingCompletedEvent(Object source, Map<AddressFileType, Path> filePaths) {
        super(source);
        this.filePaths = filePaths;
    }

    public Path getLegaldongFilePath() {
        return filePaths.get(AddressFileType.LEGAL_DONG_ADDRESS);
    }

    public Path getadmindongFilePath() {
        return filePaths.get(AddressFileType.ADMIN_DONG_ADDRESS);
    }

}
