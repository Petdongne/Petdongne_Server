package org.songeun.petdongne_server.global.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor {

    public static void unzip(Path zipPath, Path outputDir) throws IOException {
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        try(ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath.toFile()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path entrySaveTargetPath = outputDir.resolve(entry.getName()).normalize();

                if (!entrySaveTargetPath.startsWith(outputDir)) {
                    throw new IOException("Entry is outside of target dir: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(entrySaveTargetPath);
                } else{
                    Files.createDirectories(entrySaveTargetPath.getParent());
                    try (OutputStream os = Files.newOutputStream(entrySaveTargetPath)) {
                        zis.transferTo(os);
                    }
                }
            }

            zis.closeEntry();
        }
    }

}
