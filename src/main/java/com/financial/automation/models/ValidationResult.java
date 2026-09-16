package com.financial.automation.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds the result of a validation operation.
 *
 * A validation is successful when no validation errors are present.
 * When validation fails, all detected errors are collected so that
 * the caller can see the complete set of issues in one execution.
 */
public class ValidationResult {

    private final List<String> errors = new ArrayList<>();

    /**
     * Adds a validation error.
     *
     * @param error descriptive validation error
     */
    public void addError(String error) {
        errors.add(error);
    }

    /**
     * Indicates whether validation passed.
     *
     * @return true when there are no validation errors
     */
    public boolean isValid() {
        return errors.isEmpty();
    }

    /**
     * Returns all validation errors.
     *
     * @return unmodifiable list of errors
     */
    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * Returns all validation errors as a single string.
     *
     * @return formatted validation errors
     */
    public String getErrorMessage() {
        return String.join(System.lineSeparator(), errors);
    }

    /**
     * Returns the number of validation errors.
     *
     * @return error count
     */
    public int getErrorCount() {
        return errors.size();
    }

    @Override
    public String toString() {
        if (isValid()) {
            return "ValidationResult{valid=true}";
        }

        return "ValidationResult{" +
                "valid=false" +
                ", errorCount=" + errors.size() +
                ", errors=" + errors +
                '}';
    }
}