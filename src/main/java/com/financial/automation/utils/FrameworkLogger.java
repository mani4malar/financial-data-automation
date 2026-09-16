
package com.financial.automation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FrameworkLogger {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(FrameworkLogger.class);

    private FrameworkLogger() {
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void warn(String message) {
        LOGGER.warn(message);
    }

    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void error(String message, Throwable throwable) {
        LOGGER.error(message, throwable);
    }
}