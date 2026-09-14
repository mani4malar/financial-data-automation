package com.financial.automation.utils;

import com.financial.automation.models.InstrumentDetails;
import com.financial.automation.models.PositionDetails;
import com.financial.automation.models.PositionReport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ExpectedReportGenerator {

    private ExpectedReportGenerator() {
    }

    public static List<PositionReport> generate(
            List<InstrumentDetails> instruments,
            List<PositionDetails> positions) {

        Map<String, InstrumentDetails> instrumentMap = instruments.stream()
                .collect(Collectors.toMap(
                        InstrumentDetails::getId,
                        Function.identity()
                ));

        List<PositionReport> expectedReports = new ArrayList<>();

        for (PositionDetails position : positions) {

            InstrumentDetails instrument =
                    instrumentMap.get(position.getInstrumentId());

            BigDecimal totalPrice = FinancialCalculationUtils.calculateTotalPrice(position.getQuantity(), instrument.getUnitPrice());

            PositionReport report = new PositionReport();

            report.setId(null);
            report.setPositionId(position.getId());
            report.setIsin(instrument.getIsin());
            report.setQuantity(position.getQuantity());
            report.setTotalPrice(totalPrice);

            expectedReports.add(report);
        }

        return expectedReports;
    }
}