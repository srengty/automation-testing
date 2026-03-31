# Lab03: Test Automation Frameworks - Comprehensive Implementation

This lab provides hands-on experience building enterprise-grade test automation frameworks using Java, Selenium, TestNG, REST-assured, and modern automation practices.

## 🎯 Learning Objectives

By completing this lab, you will:
- Build a complete Page Object Model framework for UI automation
- Implement API testing with REST-assured for comprehensive backend testing
- Create data-driven tests with TestNG and external data sources
- Integrate reporting with Allure for professional test documentation
- Set up CI/CD pipeline integration for continuous testing
- Apply design patterns for maintainable and scalable test code

## 🏗️ Project Structure

```
Lab03/
├── README.md                           # This file
├── pom.xml                            # Maven configuration with all dependencies
├── docker-compose.yml                # Local test environment setup
├── .github/workflows/                 # CI/CD pipeline configuration
├── src/
│   ├── main/java/lab03/
│   │   ├── core/                      # Core framework components
│   │   │   ├── driver/               # WebDriver and browser management
│   │   │   ├── config/               # Configuration management
│   │   │   ├── utils/                # Utility classes and helpers
│   │   │   └── base/                 # Base classes for pages and tests
│   │   ├── pages/                    # Page Object classes
│   │   │   ├── common/               # Common page components
│   │   │   ├── ecommerce/            # E-commerce specific pages
│   │   │   └── admin/                # Admin interface pages
│   │   ├── api/                      # API client and utilities
│   │   │   ├── clients/              # REST-assured API clients
│   │   │   ├── models/               # Data models for API requests/responses
│   │   │   └── validators/           # Response validation utilities
│   │   ├── data/                     # Test data management
│   │   │   ├── factories/            # Test data factories
│   │   │   ├── providers/            # Data providers for TestNG
│   │   │   └── models/               # Test data model classes
│   │   └── mobile/                   # Mobile automation components (Appium)
│   │       ├── pages/                # Mobile page objects
│   │       └── utils/                # Mobile-specific utilities
│   └── test/java/lab03/
│       ├── ui/                       # UI automation tests
│       │   ├── smoke/                # Smoke test suite
│       │   ├── regression/           # Full regression tests
│       │   └── responsive/           # Responsive design tests
│       ├── api/                      # API automation tests
│       │   ├── functional/           # API functional tests
│       │   ├── contract/             # Contract testing
│       │   └── performance/          # API performance tests
│       ├── mobile/                   # Mobile automation tests
│       │   ├── android/              # Android-specific tests
│       │   └── ios/                  # iOS-specific tests
│       └── integration/              # End-to-end integration tests
├── src/test/resources/
│   ├── testdata/                     # Test data files
│   │   ├── users.json               # User test data
│   │   ├── products.json            # Product test data
│   │   └── scenarios.json           # Test scenario data
│   ├── config/                      # Configuration files
│   │   ├── environments/            # Environment-specific configs
│   │   └── browsers/               # Browser-specific settings
│   ├── testsuites/                 # TestNG suite configurations
│   │   ├── smoke-tests.xml         # Smoke test suite
│   │   ├── regression-tests.xml    # Regression test suite
│   │   └── api-tests.xml          # API test suite
│   └── schemas/                    # JSON schemas for API validation
├── reports/                        # Generated test reports
├── logs/                          # Test execution logs
├── screenshots/                   # Screenshots on test failures
└── target/                       # Maven build output
```

## 🚀 Quick Start Guide

### Prerequisites
- Java 17+ installed
- Maven 3.8+ installed
- Chrome, Firefox, or Edge browser
- IDE: IntelliJ IDEA, Eclipse, or VS Code
- Docker (optional, for Selenium Grid)

### Setup Instructions

1. **Clone or download the project**
   ```bash
   # The project is already set up for you
   cd Lab03
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Verify setup**
   ```bash
   mvn test -Dtest=lab03.ui.smoke.SetupVerificationTest
   ```

4. **Run smoke tests**
   ```bash
   mvn test -DsuiteXmlFile=src/test/resources/testsuites/smoke-tests.xml
   ```

### Available Test Suites

| Suite | Command | Description |
|-------|---------|-------------|
| **Smoke Tests** | `mvn test -DsuiteXmlFile=smoke-tests.xml` | Quick validation of key functionality |
| **Regression Tests** | `mvn test -DsuiteXmlFile=regression-tests.xml` | Comprehensive UI and API testing |
| **API Tests** | `mvn test -DsuiteXmlFile=api-tests.xml` | Complete API testing suite |
| **Mobile Tests** | `mvn test -DsuiteXmlFile=mobile-tests.xml` | Mobile application testing |

## 🧪 Lab Exercises

### **Exercise 1: UI Automation Framework (30 minutes)**

#### **Objective**
Implement a complete Page Object Model framework for e-commerce website automation.

#### **Tasks**
1. **Framework Setup (10 minutes)**
   - Review the `core` package structure
   - Understand `DriverManager` and `Configuration` classes
   - Examine `BasePage` and `BaseTest` implementations

2. **Page Object Implementation (15 minutes)**
   - Complete the `ProductListPage` implementation
   - Add methods to `ProductDetailPage`
   - Implement `ShoppingCartPage` functionality

3. **Test Creation (5 minutes)**
   - Create data-driven product search tests
   - Implement shopping cart workflow tests
   - Add assertion and validation methods

#### **Key Learning Points**
- Separation of concerns in framework design
- Fluent interface pattern for readable tests
- Robust wait strategies and error handling
- Screenshot capture and reporting integration

### **Exercise 2: API Testing Framework (25 minutes)**

#### **Objective**
Build comprehensive API testing capabilities using REST-assured.

#### **Tasks**
1. **API Client Setup (8 minutes)**
   - Review `BaseAPIClient` configuration
   - Understand authentication and request specifications
   - Examine response validation patterns

2. **CRUD Operations Testing (12 minutes)**
   - Implement user management API tests
   - Create product catalog API tests
   - Add order processing API validation

3. **Advanced API Testing (5 minutes)**
   - JSON schema validation implementation
   - Performance assertion integration
   - Error scenario and edge case testing

#### **Key Learning Points**
- RESTful API testing patterns
- Request/response validation strategies
- JSON schema validation for contract testing
- Integration with UI tests for end-to-end scenarios

### **Exercise 3: CI/CD Integration (15 minutes)**

#### **Objective**
Configure complete CI/CD pipeline for automated testing.

#### **Tasks**
1. **Pipeline Configuration (8 minutes)**
   - Review GitHub Actions workflow
   - Configure matrix builds for multiple browsers
   - Set up parallel test execution

2. **Reporting Integration (4 minutes)**
   - Configure Allure report generation
   - Set up test result publishing
   - Add failure notification setup

3. **Environment Management (3 minutes)**
   - Configure environment-specific testing
   - Set up secret management for credentials
   - Add deployment gates based on test results

#### **Key Learning Points**
- CI/CD best practices for test automation
- Parallel execution strategies
- Comprehensive reporting and monitoring
- Environment-specific test configuration

## 📊 Assessment Criteria

### **Technical Implementation (40%)**
- Framework architecture and design decisions
- Code quality and adherence to best practices
- Proper implementation of design patterns
- Error handling and resilience

### **Test Coverage (30%)**
- Comprehensive test scenario coverage
- Proper assertion and validation implementation
- Edge case and negative testing
- Cross-browser and cross-platform considerations

### **Integration and Automation (20%)**
- CI/CD pipeline configuration quality
- Reporting and monitoring implementation
- Environment management and configuration
- Tool integration effectiveness

### **Documentation and Maintainability (10%)**
- Code documentation quality
- Test case clarity and readability
- Framework extensibility considerations
- Knowledge sharing and collaboration

## 🛠️ Tools and Technologies

### **Core Framework**
- **Java 17+**: Modern Java features and performance
- **Maven**: Dependency management and build automation
- **TestNG**: Advanced test configuration and parallel execution
- **Selenium WebDriver**: Cross-browser UI automation
- **REST-assured**: API testing with fluent interface

### **Additional Libraries**
- **WebDriverManager**: Automatic driver management
- **Allure**: Professional test reporting
- **Jackson**: JSON processing and data binding
- **AssertJ**: Fluent assertions for readable tests
- **Logback**: Structured logging and debugging

### **Optional Enhancements**
- **Appium**: Mobile application testing
- **Docker**: Containerized test environments
- **Jenkins**: Enterprise CI/CD integration
- **Playwright**: Modern web automation alternative
- **Spring Boot**: Dependency injection and configuration

## 🔍 Debugging and Troubleshooting

### **Common Issues and Solutions**

#### **WebDriver Issues**
```bash
# Problem: Driver not found
# Solution: Verify WebDriverManager setup or driver PATH

# Problem: Element not found
# Solution: Review wait strategies and element locators

# Problem: Stale element reference
# Solution: Re-locate elements or implement retry mechanisms
```

#### **API Testing Issues**
```bash
# Problem: Authentication failures
# Solution: Verify token management and refresh logic

# Problem: JSON parsing errors
# Solution: Check response format and schema validation

# Problem: Timeout issues
# Solution: Adjust timeout settings and network configuration
```

#### **Test Execution Issues**
```bash
# Problem: Parallel execution failures
# Solution: Ensure thread-safe implementation and resource management

# Problem: Flaky tests
# Solution: Review test dependencies and data management

# Problem: CI/CD pipeline failures
# Solution: Check environment configuration and dependency management
```

## 📚 Additional Resources

### **Documentation Links**
- [Selenium Documentation](https://selenium.dev/documentation/)
- [TestNG User Guide](https://testng.org/doc/documentation-main.html)
- [REST-assured Documentation](https://rest-assured.io/)
- [Allure Framework](https://docs.qameta.io/allure/)
- [Maven Reference](https://maven.apache.org/guides/)

### **Best Practice Guides**
- Clean Code principles for test automation
- Page Object Model design patterns
- API testing strategies and patterns
- CI/CD pipeline optimization
- Test data management approaches

### **Community Resources**
- Selenium community forums and discussions
- TestNG and REST-assured GitHub repositories
- Automation testing blogs and conferences
- Open-source framework examples and templates

## 🎯 Success Metrics

### **Completion Criteria**
- All test suites execute successfully
- Framework demonstrates proper design patterns
- CI/CD pipeline is functional and reliable
- Documentation is comprehensive and clear

### **Quality Indicators**
- Test execution time is optimized
- Test maintenance effort is minimized
- Framework is easily extensible
- Reporting provides actionable insights

### **Learning Validation**
- Participants can explain framework architecture decisions
- Implementation follows established best practices
- Code demonstrates understanding of automation patterns
- Framework can be adapted to different projects

---

Ready to build a world-class automation framework? Let's get started! 🚀