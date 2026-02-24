package org.songeun.petdongne_server.global.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.songeun.petdongne_server.global.common.Image;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;

@Slf4j
@Component
public class ImageFileConverter {

    private static final Tika tika = new Tika();
    public static final String PREFIX_IMAGE_TYPE = "image/";

    public Image parse(MultipartFile file) {
        Assert.notNull(file, "The file argument cannot be null.");
        ensureImageFile(file);
        return Image.create(file);
    }

    private void ensureImageFile(MultipartFile file) {
        try {
            String mimeType = tika.detect(file.getInputStream());
            if (!mimeType.startsWith(PREFIX_IMAGE_TYPE)) {
                throw new IllegalArgumentException("Invalid file type: " + mimeType + ". Only image files are allowed.");
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error occurred while detecting file type", e);
        }
    }
}
