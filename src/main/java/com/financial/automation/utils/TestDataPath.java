package com.financial.automation.utils;

public final class TestDataPath {

    private static final String POSITIVE_PATH =
            "src/test/resources/testdata/positive/";

    private static final String NEGATIVE_PATH =
            "src/test/resources/testdata/negative/";

    private TestDataPath() {
        // Utility class
    }

    public static String positive(String fileName) {
        return POSITIVE_PATH + fileName;
    }

    public static String negative(String fileName) {
        return NEGATIVE_PATH + fileName;
    }
}