package org.songeun.petdongne_server.global.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class Image {

    private final MultipartFile file;

    public static Image create(MultipartFile imageFile) {
        return Image.builder()
                .file(imageFile)
                .build();
    }

    @Builder
    private Image(MultipartFile file) {
        this.file = file;
    }
}
