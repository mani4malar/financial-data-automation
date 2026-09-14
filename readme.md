# Financial Data Automation Framework

## Overview

This project is a Java-based test automation framework designed to validate a financial data processing application.

The framework covers:

- CSV/file validation
- Financial data validation
- Referential integrity
- Duplicate/uniqueness validation
- Financial calculations
- Transformation validation
- Position report validation
- End-to-end reconciliation
- REST API automation
- UI automation
- Negative testing
- Externalized configuration
- TestNG execution
- Extent Reports
- Logging
- Reconciliation/data evidence generation

The framework is designed using maintainable automation practices with reusable validators, utilities, API clients, page objects, models, and test classes.

---

## Business Scenario

The application processes financial position data using two input files:

1. `InstrumentDetails.csv`
2. `PositionDetails.csv`

The processed data is expected to produce:

3. `PositionReport.csv`

The high-level processing flow is:

```text
InstrumentDetails.csv
        |
        v
PositionDetails.csv
        |
        v
Financial Data Processing
        |
        v
PositionReport.csv
```

The automation framework independently calculates the expected result and compares it with the actual output.

---

## Data Processing Flow

```text
Input Files
    |
    +--> InstrumentDetails.csv
    |
    +--> PositionDetails.csv
    |
    v
File Validation
    |
    v
Data Validation
    |
    +--> Completeness
    +--> Numeric Validation
    +--> Uniqueness
    +--> Referential Integrity
    |
    v
Expected Report Generation
    |
    v
PositionReport.csv
    |
    v
Output Validation
    |
    v
Expected vs Actual Comparison
    |
    v
Reconciliation Report
```

---

## Input and Output Files

### InstrumentDetails.csv

Contains instrument/master data used during position processing.

Typical information includes:

- Instrument ID
- ISIN
- Price
- Other instrument-related attributes

### PositionDetails.csv

Contains position-level information.

Typical information includes:

- Position ID
- Instrument ID
- Quantity

### PositionReport.csv

Contains the processed position information.

The automation validates:

- Position ID
- Instrument/position relationship
- ISIN
- Quantity
- Total price
- Record count
- Duplicate records
- Expected vs actual values

---

## Validation Coverage

### 1. File Validation

The framework validates:

- File existence
- Expected file names
- File structure
- Required headers
- Missing columns
- Empty files
- Malformed rows

### 2. Completeness Validation

The framework verifies that mandatory fields are populated.

Examples:

- Instrument ID must not be blank
- Position ID must not be blank
- Quantity must be present
- ISIN must be present where required
- Price must be present where required

Both positive and negative scenarios are covered.

### 3. Numeric Validation

Numeric financial values are validated for:

- Missing values
- Invalid numeric formats
- Negative values where not permitted
- Zero values where not permitted
- Valid decimal values

Financial calculations use `BigDecimal` where precision is important.

### 4. Uniqueness Validation

The framework verifies that records expected to be unique do not contain duplicates.

Examples:

- Duplicate Instrument ID
- Duplicate Position ID
- Duplicate output records

Negative test data is included to verify that duplicate records are correctly detected.

### 5. Referential Integrity

Position records are validated against instrument master data.

```text
PositionDetails.instrumentId
            |
            v
InstrumentDetails.id
```

Every position must reference a valid instrument.

Negative test data contains invalid instrument references to verify that referential integrity failures are detected.

---

## Financial Calculation Validation

The framework independently derives the expected financial result instead of relying only on the application output.

Conceptually:

```text
Total Price = Quantity × Instrument Price
```

Financial calculations use `BigDecimal` to avoid floating-point precision problems.

The framework also applies the configured rounding/precision rules when generating the expected result.

---

## Expected Report Generation

`ExpectedReportGenerator` independently derives the expected position report from:

```text
InstrumentDetails
        +
PositionDetails
        |
        v
Expected PositionReport
```

The instrument data is indexed by instrument ID to efficiently locate the relevant instrument for each position.

The expected report is then compared against the actual `PositionReport.csv`.

This provides an independent oracle for the end-to-end validation.

---

## End-to-End Reconciliation

The end-to-end test validates the complete financial data flow.

The test performs:

1. Read instrument data
2. Read position data
3. Read actual position report
4. Validate input files
5. Validate completeness
6. Validate numeric values
7. Validate uniqueness
8. Validate referential integrity
9. Generate expected report
10. Compare expected and actual records
11. Generate reconciliation evidence

---

## Reconciliation Data Evidence

The framework generates a human-readable reconciliation report:

```text
target/data-reports/reconciliation-summary.html
```

The report contains summary information such as:

```text
Total Source Positions : 5
Total Expected Records : 5
Total Actual Records   : 5
Missing Records        : 0
Extra Records          : 0
Duplicate Records      : 0
Expected Total Quantity: 150
Actual Total Quantity  : 150
Expected Total Price   : 12500.00
Actual Total Price     : 12500.00
Overall Result         : PASS
```

The values are dynamically calculated from the source and output data and are not hardcoded.

The reconciliation report also provides record-level comparison between expected and actual output.

This provides inspectable evidence beyond a simple TestNG assertion.

---

# REST API Automation

The framework uses REST Assured for API automation.

## GET - Existing User

Endpoint:

```text
GET /api/users/2
```

Validates:

- HTTP status `200`
- Response structure
- Mandatory fields
- User data
- Response schema

## GET - Non-Existing User

Endpoint:

```text
GET /api/users/23
```

Validates:

```text
Expected HTTP status: 404
```

## POST - Create User

Endpoint:

```text
POST /api/users
```

Validates:

- HTTP status `201`
- Generated user ID
- Timestamp
- Response structure
- Response schema

## POST - Invalid Request

The framework also validates an invalid POST payload.

Expected behavior:

```text
HTTP status: 400
```

The test validates that invalid input is correctly rejected.

## API Response Time

API response time is validated against a configurable threshold.

Example:

```properties
api.response.time.threshold=3000
```

The threshold is not hardcoded inside the test.

This allows the response-time expectation to be changed through configuration without modifying the test code.

---

## API Schemas

JSON schemas are maintained under:

```text
src/test/resources/schemas/
```

Current schemas include:

```text
user-response-schema.json
create-user-response-schema.json
```

These schemas are used to validate API response structure.

---

## API Authentication / Rate Limiting

The ReqRes API may enforce anonymous request limits.

If an API key is required, it should be supplied through an environment variable rather than committed to source control.

Example PowerShell configuration:

```powershell
$env:REQRES_API_KEY="your-api-key"
```

The API key must not be stored in:

- Java source code
- `config.properties`
- Git repository
- README
- Test data

This keeps credentials outside the repository.

---

# UI Automation

Selenium WebDriver is used for lightweight UI automation.

The framework uses:

- Page Object Model
- `BasePage`
- `DriverFactory`
- Explicit waits
- Configurable browser
- Configurable timeout
- Screenshot support

## Current UI Coverage

The UI automation focuses on functionality available in the current application.

The current UI tests validate the ReqRes homepage and available endpoint interaction.

The endpoint interaction verifies that the UI can execute the selected request and display the expected response.

## Legacy Support Page

Some versions of the assessment reference a ReqRes Support page.

The Support page is not available in the current application version used by this framework.

Therefore, no artificial or legacy UI test has been added for a page that is no longer present.

The UI automation focuses on functionality currently available in the application.

---

# Negative Testing Strategy

Negative test data is maintained separately from positive test data.

Structure:

```text
src/test/resources/testdata/
├── positive/
└── negative/
```

Negative scenarios include:

- Missing mandatory fields
- Invalid numeric values
- Negative values
- Duplicate records
- Invalid instrument references
- Malformed rows
- Missing columns
- Empty files
- Invalid output records
- Transformation mismatches

This ensures that validation logic is tested for both valid and invalid data.

---

# Project Architecture

The framework follows a layered architecture.

```text
Tests
  |
  +-------------------+
  |                   |
  v                   v
API Layer          UI Layer
  |                   |
  v                   v
ApiClient          Page Objects
  |
  v
Models / Data
  |
  v
Validators
  |
  v
Utilities
  |
  v
Configuration
```

---

# Project Structure

```text
financial-data-automation/
│
├── .gitignore
├── pom.xml
├── readme.md
│
└── src/
    │
    ├── main/
    │   └── java/
    │       └── com/
    │           └── financial/
    │               └── automation/
    │
    │                   ├── api/
    │                   │   └── ApiClient.java
    │                   │
    │                   ├── config/
    │                   │   └── ConfigReader.java
    │                   │
    │                   ├── constants/
    │                   │
    │                   ├── models/
    │                   │   ├── InstrumentDetails.java
    │                   │   ├── PositionDetails.java
    │                   │   ├── PositionReport.java
    │                   │   └── api/
    │                   │       └── request/
    │                   │           └── CreateUserRequest.java
    │                   │
    │                   ├── readers/
    │                   │   └── CsvReader.java
    │                   │
    │                   ├── ui/
    │                   │   ├── BasePage.java
    │                   │   ├── DriverFactory.java
    │                   │   └── HomePage.java
    │                   │
    │                   ├── utils/
    │                   │   ├── ExpectedReportGenerator.java
    │                   │   ├── FinancialCalculationUtils.java
    │                   │   ├── ReconciliationReportGenerator.java
    │                   │   ├── ScreenshotUtils.java
    │                   │   └── TestDataPath.java
    │                   │
    │                   └── validators/
    │                       ├── CompletenessValidator.java
    │                       ├── FileValidator.java
    │                       ├── InstrumentCompletenessValidator.java
    │                       ├── InstrumentNumericValidator.java
    │                       ├── NumericValidator.java
    │                       ├── OutputValidator.java
    │                       ├── PositionCompletenessValidator.java
    │                       ├── PositionNumericValidator.java
    │                       ├── ReferentialIntegrityValidator.java
    │                       └── UniquenessValidator.java
    │
    └── test/
        │
        ├── java/
        │   └── com/
        │       └── financial/
        │           └── automation/
        │
        │               ├── base/
        │               │
        │               ├── listeners/
        │               │   └── ExtentReportListener.java
        │               │
        │               └── tests/
        │                   │
        │                   ├── api_test/
        │                   │   ├── ApiGetTest.java
        │                   │   └── ApiPostTest.java
        │                   │
        │                   ├── data_test/
        │                   │   │
        │                   │   ├── endtoend/
        │                   │   │   └── EndToEndReconciliationTest.java
        │                   │   │
        │                   │   ├── file/
        │                   │   │   ├── DataReadTest.java
        │                   │   │   ├── EmptyFileValidationTest.java
        │                   │   │   ├── FileValidationTest.java
        │                   │   │   ├── HeaderValidationTest.java
        │                   │   │   ├── InvalidFileNameValidationTest.java
        │                   │   │   ├── MalformedRowValidationTest.java
        │                   │   │   └── MissingColumnValidationTest.java
        │                   │   │
        │                   │   ├── instrument/
        │                   │   │   ├── InstrumentCompletenessTest.java
        │                   │   │   ├── InstrumentNumericTest.java
        │                   │   │   ├── NegativeInstrumentCompletenessTest.java
        │                   │   │   └── NegativeInstrumentNumericTest.java
        │                   │   │
        │                   │   ├── integrity/
        │                   │   │   ├── NegativeReferentialIntegrityTest.java
        │                   │   ├── NegativeUniquenessTest.java
        │                   │   ├── ReferentialIntegrityTest.java
        │                   │   └── UniquenessTest.java
        │                   │
        │                   ├── output/
        │                   │   └── NegativeOutputIntegrityTest.java
        │                   │
        │                   ├── position/
        │                   │   ├── NegativePositionCompletenessTest.java
        │                   │   ├── NegativePositionNumericTest.java
        │                   │   ├── PositionCompletenessTest.java
        │                   │   └── PositionNumericTest.java
        │                   │
        │                   ├── transformation/
        │                   │   ├── AggregationTest.java
        │                   │   ├── FinancialCalculationTest.java
        │                   │   ├── NegativeTransformationTest.java
        │                   │   └── TransformationTest.java
        │                   │
        │                   └── unit/
        │                       ├── CompletenessValidatorTest.java
        │                       ├── NegativeNumericDataTest.java
        │                       └── NumericValidatorTest.java
        │
        │                   └── ui_test/
        │                       └── UiPageTest.java
        │
        └── resources/
            ├── config.properties
            ├── testng.xml
            │
            ├── schemas/
            │   ├── create-user-response-schema.json
            │   └── user-response-schema.json
            │
            └── testdata/
                │
                ├── positive/
                │   ├── InstrumentDetails.csv
                │   ├── PositionDetails.csv
                │   └── PositionReport.csv
                │
                └── negative/
                    ├── file/
                    ├── instrument/
                    ├── integrity/
                    ├── output/
                    ├── position/
                    └── transformation/
```

---

# Configuration

Configuration is externalized using:

```text
src/test/resources/config.properties
```

Configuration includes items such as:

```properties
api.base.url=...
ui.base.url=...
browser=chrome
implicit.wait=...
explicit.wait=...
api.response.time.threshold=3000
```

Actual values can be changed without modifying test implementation.

Sensitive values such as API keys should be supplied through environment variables.

---

# Test Data Management

Positive test data:

```text
src/test/resources/testdata/positive/
```

Negative test data:

```text
src/test/resources/testdata/negative/
```

Test data is separated from test implementation so that scenarios can be changed without modifying Java test logic.

---

# TestNG

TestNG is used as the test execution framework.

Tests are grouped using categories such as:

```text
api
ui
regression
data
```

The TestNG suite configuration is located at:

```text
src/test/resources/testng.xml
```

---

# Extent Reports

Extent Reports are integrated using:

```text
ExtentReportListener.java
```

The listener captures test execution results and provides a human-readable HTML report.

Generated reports are available under the Maven `target` directory after execution.

---

# Screenshots

Selenium screenshots are captured through:

```text
ScreenshotUtils.java
```

Screenshots are useful when debugging UI failures.

Generated artifacts are stored under the Maven `target` directory.

---

# How to Run

## Prerequisites

Install:

- Java 17 or higher
- Maven
- Git
- Chrome/Firefox/Edge if UI tests are required

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

## Clone the Repository

```bash
git clone <repository-url>
```

Move into the project:

```bash
cd financial-data-automation
```

## Run All Tests

```bash
mvn clean test
```

## Run Without Cleaning

```bash
mvn test
```

## Run a Specific Test

Example:

```bash
mvn -Dtest=ApiGetTest test
```

Another example:

```bash
mvn -Dtest=EndToEndReconciliationTest test
```

---

# Generated Test Evidence

After running:

```bash
mvn clean test
```

Maven generates execution artifacts under:

```text
target/
```

Important evidence includes:

```text
target/
├── surefire-reports/
├── data-reports/
│   └── reconciliation-summary.html
└── ...
```

The exact generated files can vary depending on the test execution and reporting configuration.

---

# Failure Triage

When a test fails, the framework provides multiple sources of evidence:

```text
1. TestNG/Surefire result
2. Extent Report
3. Reconciliation HTML
4. Record-level comparison
5. Screenshot for UI failures
6. Console/log output
```

For data failures, the reconciliation report can help identify whether the issue is:

- Missing record
- Extra record
- Duplicate record
- Quantity mismatch
- Price mismatch
- ISIN mismatch
- Transformation/calculation mismatch

For API failures, inspect:

- HTTP status
- Response body
- Schema validation
- Response time
- API availability/rate limits

---

# Design Decisions

## Reusable Validators

Validation rules are implemented as reusable classes instead of embedding all validation logic inside test methods.

Examples:

```text
CompletenessValidator
NumericValidator
UniquenessValidator
ReferentialIntegrityValidator
OutputValidator
```

This keeps test classes focused on test scenarios.

## Independent Expected Result

Expected financial output is calculated independently using:

```text
ExpectedReportGenerator
```

This avoids simply validating the application output against itself.

## BigDecimal for Financial Calculations

`BigDecimal` is used for financial calculations where decimal precision matters.

This avoids common floating-point calculation issues associated with `double`.

## Page Object Model

UI interactions are encapsulated within page classes.

This keeps Selenium locators and interaction logic separate from test methods.

## Externalized Configuration

URLs, browser settings, timeouts, response-time thresholds and other environment-specific values are externalized.

This makes the framework easier to run in different environments.

## Positive and Negative Data Separation

Positive and negative datasets are stored separately.

This makes test intent clear and prevents invalid data from accidentally being used by positive scenarios.

---

# Technology Stack

| Technology | Purpose |
|---|---|
| Java 17+ | Programming language |
| Maven | Build and dependency management |
| TestNG | Test execution |
| REST Assured | REST API automation |
| Selenium WebDriver | UI automation |
| Extent Reports | HTML reporting |
| BigDecimal | Financial calculations |
| CSV | Financial test data |
| JSON Schema | API response validation |
| Git/GitHub | Source control |

---

# Key Framework Components

### `ApiClient`

Encapsulates REST API communication.

### `CsvReader`

Reads CSV data into Java model objects.

### `ExpectedReportGenerator`

Independently generates expected financial results.

### `ReconciliationReportGenerator`

Creates the end-to-end reconciliation HTML evidence.

### `FinancialCalculationUtils`

Contains reusable financial calculation logic.

### Validators

Validate individual data quality rules.

### `DriverFactory`

Creates Selenium WebDriver instances based on configuration.

### Page Objects

Encapsulate UI interaction and locators.

### `ExtentReportListener`

Integrates TestNG execution with Extent Reports.

### `ConfigReader`

Loads externalized configuration values.

---

# Repository Hygiene

Generated build artifacts should not be committed to Git.

The following directory is generated by Maven:

```text
target/
```

It should remain excluded through `.gitignore`.

Sensitive information must also never be committed, including:

```text
API keys
Passwords
Tokens
Machine-specific paths
Environment secrets
```

---

# Submission Checklist

Before submitting the project, verify:

- [ ] Java 17+ is used
- [ ] Maven build works
- [ ] `mvn clean test` executes
- [ ] CSV validation is implemented
- [ ] Completeness validation is implemented
- [ ] Numeric validation is implemented
- [ ] Uniqueness validation is implemented
- [ ] Referential integrity is implemented
- [ ] Transformation is validated
- [ ] Financial calculation is validated
- [ ] Expected report is independently generated
- [ ] Actual vs expected output is reconciled
- [ ] Reconciliation HTML is generated
- [ ] Negative scenarios are covered
- [ ] API GET tests are implemented
- [ ] API POST tests are implemented
- [ ] API response schemas are validated
- [ ] API response time is configurable
- [ ] UI automation is implemented for current functionality
- [ ] Legacy unavailable UI functionality is documented
- [ ] Extent Reports are generated
- [ ] Screenshots are available for UI failures
- [ ] Configuration is externalized
- [ ] No secrets are committed
- [ ] No `target/` artifacts are committed
- [ ] README is included
- [ ] Test strategy is included
- [ ] Git repository contains only required source/configuration files

---

# Final Execution Flow

```text
                 +----------------------+
                 |    TestNG Suite      |
                 +----------+-----------+
                            |
          +-----------------+-----------------+
          |                 |                 |
          v                 v                 v
     Data Tests         API Tests         UI Tests
          |                 |                 |
          v                 v                 v
    CSV Validation      REST Assured       Selenium
          |                 |                 |
          v                 v                 v
    Business Rules     Schema/Status       Page Objects
          |
          v
 Expected Report
          |
          v
 Actual Report
          |
          v
  Reconciliation
          |
          v
 HTML Evidence
          |
          v
   PASS / FAIL
```

---

# Conclusion

This framework provides an end-to-end automation solution for financial data validation.

It combines:

- Data quality validation
- Financial calculation validation
- Independent expected-result generation
- Output reconciliation
- REST API automation
- UI automation
- Negative testing
- Configurable execution
- Automated reporting
- Test evidence generation

The design focuses on maintainability, reusability, financial-data accuracy, clear failure diagnosis, and practical automation engineering standards.
