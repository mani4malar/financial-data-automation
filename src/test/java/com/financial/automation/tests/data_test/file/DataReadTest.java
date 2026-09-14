package com.financial.automation.tests.data_test.file;

import com.financial.automation.config.ConfigReader;
import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.models.PositionReport;
import com.financial.automation.readers.CsvReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class DataReadTest {

    private final CsvReader csvReader = new CsvReader();

    @Test
    public void shouldReadInstrumentDetails() throws IOException {

        String filePath = ConfigReader.get("instrument.file");

        List<InstrumentDetails> instruments =
                csvReader.readInstrumentDetails(filePath);

        Assert.assertFalse(
                instruments.isEmpty(),
                "InstrumentDetails should contain records"
        );

        System.out.println("Instrument records: " + instruments.size());
        System.out.println("First instrument: " + instruments.get(0));
    }

    @Test
    public void shouldReadPositionDetails() throws IOException {

        String filePath = ConfigReader.get("position.file");

        List<PositionDetails> positions =
                csvReader.readPositionDetails(filePath);

        Assert.assertFalse(
                positions.isEmpty(),
                "PositionDetails should contain records"
        );

        System.out.println("Position records: " + positions.size());
        System.out.println("First position: " + positions.get(0));
    }

    @Test
    public void shouldReadPositionReport() throws IOException {

        String filePath = ConfigReader.get("report.file");

        List<PositionReport> reports =
                csvReader.readPositionReport(filePath);

        Assert.assertFalse(
                reports.isEmpty(),
                "PositionReport should contain records"
        );

        System.out.println("Report records: " + reports.size());
        System.out.println("First report: " + reports.get(0));
    }
}