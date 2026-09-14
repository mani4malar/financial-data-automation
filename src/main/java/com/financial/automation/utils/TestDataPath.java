package com.financial.automation.utils;

public final class TestDataPath {

    private static final String POSITIVE_PATH =
            "src/test/resources/testdata/positive/";

    private static final String NEGATIVE_PATH =
            "src/test/resources/testdata/negative/";

    private TestDataPath() {
    }

    public static String positive(String fileName) {
        return POSITIVE_PATH + fileName;
    }

    public static String negativeFile(String fileName) {
        return NEGATIVE_PATH + "file/" + fileName;
    }

    public static String negativeInstrument(String fileName) {
        return NEGATIVE_PATH + "instrument/" + fileName;
    }

    public static String negativePosition(String fileName) {
        return NEGATIVE_PATH + "position/" + fileName;
    }

    public static String negativeIntegrity(String fileName) {
        return NEGATIVE_PATH + "integrity/" + fileName;
    }

    public static String negativeTransformation(String fileName) {
        return NEGATIVE_PATH + "transformation/" + fileName;
    }

    public static String negativeOutput(String fileName) {
        return NEGATIVE_PATH + "output/" + fileName;
    }
}