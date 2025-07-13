package org.songeun.petdongne_server.global.file;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class CsvHeaderNameParsing implements Parser<String> {

    @Override
    public List<String> parse(BufferedReader reader) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                return List.of(csvReader.readNext());
            } catch (IOException | CsvValidationException e) {
                throw new RuntimeException(e);
            }
    }

}
