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
