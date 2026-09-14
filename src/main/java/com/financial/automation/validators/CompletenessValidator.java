package com.financial.automation.validators;

public final class CompletenessValidator {

    private CompletenessValidator() {
        // Utility class
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}