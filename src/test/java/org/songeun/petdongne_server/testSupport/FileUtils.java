package org.songeun.petdongne_server.testSupport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    public static void makeZip(Map<Path, String> zipEntries, Path ouputPath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(ouputPath.toFile()))) {
            for (Map.Entry<Path, String> entry : zipEntries.entrySet()) {
                ZipEntry zipEntry = new ZipEntry(entry.getKey().toString());
                zos.putNextEntry(zipEntry);

                if (entry.getValue() != null) {
                    zos.write(entry.getValue().getBytes(StandardCharsets.UTF_8));
                }

                zos.closeEntry();
            }

        }
    }

    public static void createExcelFile(Path filePath, List<List<String>> rows) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Sheet1");

            for (int i = 0; i < rows.size(); i++) {
                Row row = sheet.createRow(i);
                List<String> cells = rows.get(i);
                for (int j = 0; j < cells.size(); j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(cells.get(j));
                }
            }

            try (OutputStream os = Files.newOutputStream(filePath)) {
                workbook.write(os);
            }
        }
    }

}
