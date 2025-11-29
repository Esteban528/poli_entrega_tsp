package com.poligran.tsp.sistema_encuestas.utils;

import org.springframework.stereotype.Component;

import lombok.Setter;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

@Component
@Setter
public class CsvComponent {
    int temporalN;

    public void writeStudentsToCsv(Writer w, Map<String, List<String>> content) {
        CSVFormat format = CSVFormat.DEFAULT;
        List<String> entries;
        try (CSVPrinter printer = new CSVPrinter(w, format)) {
            printer.printRecord(content.keySet());

            content.keySet()
                    .stream()
                    .limit(1)
                    .forEach(c -> setTemporalN(content.get(c).size()));

            for (int i = 0; i < temporalN; i++) {
                entries = new LinkedList<>();
                for (String key : content.keySet()) {
                    entries.add(content.get(key).get(i));
                }
                printer.printRecord(entries);
            }
            printer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
