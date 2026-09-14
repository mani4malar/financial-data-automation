package com.financial.automation.validators;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public final class FileValidator {

    private FileValidator() {
        // Utility class
    }

    public static boolean fileExists(String filePath) {

        return Files.exists(Path.of(filePath));
    }

    public static boolean isEmpty(String filePath) {
    try {
        return Files.size(Path.of(filePath)) == 0;
    } catch (IOException e) {
        throw new RuntimeException(
                "Unable to determine file size: " + filePath,
                e
        );
    }
}

public static boolean hasExpectedHeaders(
        String filePath,
        String... expectedHeaders) {

    try {
        String firstLine = Files.readAllLines(Path.of(filePath))
                .stream()
                .findFirst()
                .orElse("");

        List<String> actualHeaders =
                Arrays.stream(firstLine.split(",", -1))
                        .map(String::trim)
                        .toList();

        return actualHeaders.equals(
                Arrays.asList(expectedHeaders)
        );

    } catch (IOException e) {
        throw new RuntimeException(
                "Unable to read file headers: " + filePath,
                e
        );
    }
}

public static boolean hasRequiredHeaders(
        String filePath,
        String... requiredHeaders) {

    try (CSVParser parser = CSVParser.parse(
            Path.of(filePath),
            java.nio.charset.StandardCharsets.UTF_8,
            CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build())) {

        List<String> actualHeaders =
                parser.getHeaderNames();

        return Arrays.stream(requiredHeaders)
                .allMatch(actualHeaders::contains);

    } catch (IOException e) {
        throw new RuntimeException(
                "Unable to read file headers: " + filePath, e);
    }
}
}