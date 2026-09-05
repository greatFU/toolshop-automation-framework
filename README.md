# Toolshop Automation Framework

UI automation framework for the [Practice Software Testing](https://practicesoftwaretesting.com/) e-commerce application.

The project demonstrates a maintainable Selenium test framework with Page Object Model, reusable business flows, data-driven tests, parallel execution, reporting, and a Jenkins CI pipeline that provisions the application under test with Docker Compose.

## Highlights

- Page Object Model with reusable components
- Reusable business flows for multi-step scenarios
- Data-driven testing with TestNG DataProviders and JSON
- Smoke and Regression Maven profiles
- Parallel execution at TestNG class level
- Chrome, Firefox, and Edge support
- Headless execution for CI
- ExtentReports with screenshots on test failure
- Jenkins Pipeline as Code
- Docker Compose test environment with database reset and readiness checks

## Tech Stack

- Java 21
- Selenium WebDriver
- TestNG
- Maven
- Jackson
- ExtentReports
- Jenkins
- Docker / Docker Compose
- Git / GitHub

## Test Coverage

Current UI coverage includes:

- Home page loading
- Product cards visibility
- Valid admin authentication
- Invalid authentication
- Product details validation
- Add one product to cart
- Add multiple products to cart
- Change product quantity
- Product line-total validation
- Cart-total validation
- Remove selected product
- Empty-cart validation
- Logged-in checkout navigation

## Framework Structure

```text
src/
├── main/java/com/fernandoqa/
│   ├── components/       Reusable UI components and common waits
│   ├── flows/            Reusable business workflows
│   └── pageobjects/      Page Objects and page-specific actions
│
└── test/
    ├── java/com/fernandoqa/
    │   ├── testcomponents/   Driver setup, listeners and test infrastructure
    │   └── tests/            TestNG test classes
    └── resources/
        └── testdata/         JSON test data

testSuites/
├── regression.xml
└── smoke.xml
```

Assertions remain in the test layer, while Page Objects expose page behavior and reusable flows encapsulate multi-step business operations.

## Running Tests

Prerequisites:

- Java 21
- Maven
- Chrome, Firefox, or Edge

Run the default Regression suite:

```bash
mvn clean test
```

Run Regression explicitly:

```bash
mvn clean test -PRegression
```

Run Smoke:

```bash
mvn clean test -PSmoke
```

Run Regression in Firefox headless:

```bash
mvn clean test -PRegression -Dbrowser=firefox -Dheadless=true
```

Available runtime properties:

```text
-Dbrowser=chrome|firefox|edge
-Dheadless=true|false
-DbaseUrl=<application-url>
```

## Reporting

The framework uses TestNG listeners and ExtentReports.

When a UI test fails, the framework captures a screenshot and attaches it to the report.

Generated output:

```text
reports/index.html
screenshots/
target/surefire-reports/
```

## Jenkins CI Pipeline

The repository contains a declarative `Jenkinsfile`.

The pipeline:

```text
Checkout framework
      ↓
Verify Maven and Docker
      ↓
Checkout Toolshop application
      ↓
Build and start Toolshop with Docker Compose
      ↓
Wait for Composer
      ↓
Reset and seed the test database
      ↓
Wait for API and UI readiness
      ↓
Run Selenium tests
      ↓
Publish test results and artifacts
      ↓
Stop Docker services
```

Build parameters:

| Parameter | Values |
| --- | --- |
| `BROWSER` | `chrome`, `firefox`, `edge` |
| `HEADLESS` | `true`, `false` |
| `PROFILE` | `Regression`, `Smoke` |

The Selenium tests run against the Docker-hosted application at:

```text
http://localhost:4200
```

Surefire XML results are published to Jenkins as test results. ExtentReports, Surefire output, and failure screenshots are archived as build artifacts.

If the pipeline fails after Docker startup, container status and recent Docker logs are collected for diagnostics. Docker cleanup is attempted in the pipeline `post` section even after a failed test run.

## Docker Test Environment

The CI pipeline uses the official Practice Software Testing Docker Compose configuration to create an isolated local application environment.

Useful commands when working with the application manually:

```bash
docker compose up -d --build
docker compose ps
docker compose logs
docker compose exec -T laravel-api php artisan migrate:fresh --seed
docker compose down
```

The UI is available at:

```text
http://localhost:4200
```

The API is available at:

```text
http://localhost:8091
```

> `migrate:fresh --seed` is destructive and should only be used with a dedicated test database.

## Known Issue

`adminUserMenuShouldBeDisplayedAfterLogin` documents a confirmed application defect and belongs to the `knownBug` TestNG group. The regular Regression suite excludes this group.

## Project Goal

This project was built as a portfolio QA Automation framework focused on maintainability, reusable test design, reliable synchronization, CI execution, reporting, and reproducible test environments.