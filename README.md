# Toolshop Automation Framework

UI test automation framework for the Toolshop e-commerce application.

## Technologies

- Java 21
- Selenium WebDriver
- TestNG
- Maven

## Application under test

Practice Software Testing:
https://practicesoftwaretesting.com/

## Implemented test coverage

- Home page loading
- Product cards visibility
- Valid admin authentication
- Invalid authentication
- Product details validation
- Add one product to cart
- Add multiple products to cart
- Change product quantity
- Validate product line total
- Validate cart total
- Remove selected product
- Validate empty cart state
- Logged-in checkout navigation

## Project structure

- pageobjects — page-specific elements and actions
- components — reusable page components and waits
- flows — reusable business workflows
- testcomponents — browser setup and teardown
- tests — TestNG test classes

## Test data

Test data is stored in JSON files under:

`src/test/resources/testdata`

TestNG DataProviders deserialize JSON data using Jackson
and pass each data set to the corresponding test.

## Reporting

The framework uses TestNG listeners and ExtentReports.

When a test fails, the framework automatically:

- records the exception;
- captures a browser screenshot;
- attaches the screenshot to the HTML report.

Generated output:

- Extent report: `reports/index.html`
- Failure screenshots: `screenshots/`
- Surefire results: `target/surefire-reports/`

## Known issues

`adminUserMenuShouldBeDisplayedAfterLogin` documents a confirmed
application defect and belongs to the `knownBug` regression group.

The regression suite excludes this group through
`testSuites/regression.xml`.

## Running tests

Run the default regression suite:

```bash
mvn clean test
```

Run regression explicitly:

```bash
mvn clean test -PRegression
```

Run smoke tests:

```bash
mvn clean test -PSmoke
```

Run regression in Firefox headless:

```bash
mvn clean test -PRegression -Dbrowser=firefox -Dheadless=true
```

Supported browsers:

- Chrome
- Firefox
- Edge

## CI/CD

The project has been integrated with a local Jenkins CI job.

The Jenkins job:
- checks out the latest project version from GitHub;
- runs TestNG suites through Maven;
- supports Chrome, Firefox and Edge;
- supports headless execution;
- supports Smoke and Regression suite selection;
- publishes Surefire test results;
- archives ExtentReports and Surefire artifacts.
