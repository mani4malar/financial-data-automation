package com.financial.automation.readers;

import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.models.PositionReport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public List<InstrumentDetails> readInstrumentDetails(String filePath)
            throws IOException {

        List<InstrumentDetails> instruments = new ArrayList<>();

        try (CSVParser parser = CSVParser.parse(
                Path.of(filePath),
                java.nio.charset.StandardCharsets.UTF_8,
                CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .build())) {

            for (CSVRecord record : parser) {

                InstrumentDetails instrument = new InstrumentDetails();

                instrument.setId(record.get("ID"));
                instrument.setName(record.get("Name"));
                instrument.setIsin(record.get("ISIN"));
                instrument.setUnitPrice(parseBigDecimal(record.get("Unit Price")));

                instruments.add(instrument);
            }
        }

        return instruments;
    }

    public List<PositionDetails> readPositionDetails(String filePath)
            throws IOException {

        List<PositionDetails> positions = new ArrayList<>();

        try (CSVParser parser = CSVParser.parse(
                Path.of(filePath),
                java.nio.charset.StandardCharsets.UTF_8,
                CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .build())) {

            for (CSVRecord record : parser) {

                PositionDetails position = new PositionDetails();

                position.setId(record.get("ID"));
                position.setInstrumentId(record.get("InstrumentID"));
                position.setQuantity(parseBigDecimal(record.get("Quantity")));
                positions.add(position);
            }
        }

        return positions;
    }

    public List<PositionReport> readPositionReport(String filePath)
            throws IOException {

        List<PositionReport> reports = new ArrayList<>();

        try (CSVParser parser = CSVParser.parse(
                Path.of(filePath),
                java.nio.charset.StandardCharsets.UTF_8,
                CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .build())) {

            for (CSVRecord record : parser) {

                PositionReport report = new PositionReport();

                report.setId(record.get("ID"));
                report.setPositionId(record.get("PositionID"));
                report.setIsin(record.get("ISIN"));
                report.setQuantity(parseBigDecimal(record.get("Quantity")));
                report.setTotalPrice(parseBigDecimal(record.get("Total Price")));
                reports.add(report);
            }
        }

        return reports;
    }

   private BigDecimal parseBigDecimal(String value) {
    if (value == null || value.trim().isEmpty()) {
        return null;
    }

    try {
        return new BigDecimal(value.trim());
    } catch (NumberFormatException e) {
        return null;
    }
}
}