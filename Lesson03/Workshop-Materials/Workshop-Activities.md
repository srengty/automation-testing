# Workshop Activities - Test Automation Frameworks

This document contains 5 hands-on workshop activities designed to reinforce key concepts in test automation framework design and implementation.

---

## 🎯 Workshop 1: Framework Architecture Design Challenge

### **Duration:** 10 minutes
### **Group Size:** 2-3 participants
### **Difficulty:** Intermediate

### **Scenario**
You're tasked with designing a test automation framework for a multi-platform e-commerce application that includes:
- Web application (desktop and mobile)
- REST APIs
- Mobile apps (iOS and Android)
- Microservices backend

### **Challenge Instructions**

1. **Requirements Analysis (3 minutes)**
   - Identify key testing requirements for each platform
   - List technical constraints and dependencies
   - Define success criteria for the framework

2. **Architecture Design (5 minutes)**
   - Draw a high-level architecture diagram
   - Define component relationships and data flow
   - Select appropriate design patterns
   - Choose technology stack for each layer

3. **Presentation Prep (2 minutes)**
   - Prepare 2-minute solution overview
   - Identify key design decisions and trade-offs
   - Prepare to justify technology choices

### **Design Considerations**
- **Scalability:** How will the framework handle growth?
- **Maintainability:** How easy is it to modify and extend?
- **Reusability:** Can components be shared across projects?
- **Reliability:** How does it handle failures and flaky tests?
- **Observability:** What reporting and monitoring capabilities exist?

### **Deliverables**
- Architecture diagram (hand-drawn or digital)
- Technology stack decision matrix
- Component responsibility matrix
- 2-minute presentation to class

### **Evaluation Criteria**
- **Architecture Quality (40%):** Clear separation of concerns, appropriate patterns
- **Technology Choices (30%):** Justified selections based on requirements
- **Scalability Considerations (20%):** Future-proofing and growth planning
- **Presentation Clarity (10%):** Clear communication of design decisions

---

## 🛠️ Workshop 2: Page Object Pattern Implementation

### **Duration:** 10 minutes
### **Individual or Pair Exercise**
### **Difficulty:** Beginner to Intermediate

### **Scenario**
Implement a robust Page Object Model for the login functionality of a sample web application with advanced features like multi-factor authentication.

### **Setup Code**
```java
// Base Page Object class - extend this for all page objects
public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
    
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
}
```

### **Implementation Tasks**

#### **Task 1: Basic Page Object (4 minutes)**
Create a `LoginPage` class with the following elements and methods:

**Elements to locate:**
- Username field (`#username`)
- Password field (`#password`)
- Login button (`.login-button`)
- Error message (`.error-message`)
- Remember me checkbox (`#remember-me`)
- Forgot password link (`.forgot-password`)

**Methods to implement:**
- `enterUsername(String username)`
- `enterPassword(String password)`
- `clickLogin()`
- `getErrorMessage()`
- `isLoginButtonEnabled()`
- `clickForgotPassword()`

#### **Task 2: Advanced Features (4 minutes)**
Enhance the LoginPage with:

**Fluent Interface:**
```java
public LoginPage enterCredentials(String username, String password) {
    enterUsername(username);
    enterPassword(password);
    return this;
}

public DashboardPage submitLogin() {
    clickLogin();
    return new DashboardPage(driver);
}
```

**Validation Methods:**
```java
public boolean isLoggedIn() {
    // Implementation here
}

public boolean hasValidationError() {
    // Implementation here
}
```

#### **Task 3: Test Implementation (2 minutes)**
Write a test method that uses your Page Object:

```java
@Test
public void testValidLogin() {
    // TODO: Implement test using your LoginPage
    // Navigate to login page
    // Enter valid credentials
    // Verify successful login
}

@Test
public void testInvalidLogin() {
    // TODO: Implement negative test case
    // Test invalid credentials
    // Verify error message displayed
}
```

### **Solution Framework**
```java
public class LoginPage extends BasePage {
    @FindBy(id = "username")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(css = ".login-button")
    private WebElement loginButton;
    
    // TODO: Add remaining elements
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    // TODO: Implement methods
    public LoginPage enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.clear();
        usernameField.sendKeys(username);
        return this;
    }
    
    // TODO: Complete implementation
}
```

### **Bonus Challenges**
- Add explicit wait strategies for each interaction
- Implement screenshot capture for failed validations
- Create a method to handle dynamic loading states
- Add logging for all page interactions

---

## 🌐 Workshop 3: REST-assured API Testing

### **Duration:** 10 minutes
### **Individual Exercise**
### **Difficulty:** Intermediate

### **Scenario**
Create a comprehensive API test suite for a User Management service with CRUD operations and authentication.

### **API Specification**
```yaml
# User Management API
Base URL: https://api.example.com
Authentication: Bearer Token

Endpoints:
POST   /api/v1/users          # Create user
GET    /api/v1/users/{id}     # Get user by ID
GET    /api/v1/users          # List all users
PUT    /api/v1/users/{id}     # Update user
DELETE /api/v1/users/{id}     # Delete user

User Object:
{
  "id": "integer",
  "name": "string",
  "email": "string",
  "role": "string",
  "active": "boolean",
  "createdAt": "datetime"
}
```

### **Implementation Tasks**

#### **Task 1: Base Setup (3 minutes)**
```java
public class UserApiTest {
    private static final String BASE_URI = "https://api.example.com";
    private static final String API_TOKEN = "your-api-token";
    
    @BeforeClass
    public void setup() {
        // TODO: Configure REST-assured base settings
        RestAssured.baseURI = BASE_URI;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + API_TOKEN)
                .build();
    }
    
    // TODO: Create test data builder
    public User createTestUser() {
        return User.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .role("user")
                .active(true)
                .build();
    }
}
```

#### **Task 2: CRUD Test Implementation (5 minutes)**
```java
@Test
public void testCreateUser() {
    User newUser = createTestUser();
    
    ValidatableResponse response = given()
            .body(newUser)
        .when()
            .post("/api/v1/users")
        .then()
            .statusCode(201)
            .contentType(ContentType.JSON);
    
    // TODO: Add response validation
    // Verify: user ID returned, all fields match, timestamps exist
}

@Test
public void testGetUser() {
    // TODO: Implement GET user test
    // Create user first, then retrieve by ID
    // Validate all fields match
}

@Test
public void testUpdateUser() {
    // TODO: Implement PUT user test
    // Create user, update specific fields, verify changes
}

@Test
public void testDeleteUser() {
    // TODO: Implement DELETE user test
    // Create user, delete, verify 404 on subsequent GET
}

@Test
public void testListUsers() {
    // TODO: Implement GET users list test
    // Create multiple users, verify list contains them
}
```

#### **Task 3: Advanced Scenarios (2 minutes)**
```java
@Test
public void testCreateUserWithInvalidData() {
    given()
        .body("{\"name\": \"\", \"email\": \"invalid-email\"}")
    .when()
        .post("/api/v1/users")
    .then()
        .statusCode(400)
        .body("errors", hasSize(greaterThan(0)));
}

@Test
public void testUnauthorizedAccess() {
    given()
        .header("Authorization", "Bearer invalid-token")
    .when()
        .get("/api/v1/users")
    .then()
        .statusCode(401);
}

// TODO: Implement additional negative test cases
```

### **Helper Methods**
```java
// JSON Schema Validation
public void validateUserSchema(ValidatableResponse response) {
    response.body(matchesJsonSchemaInClasspath("user-schema.json"));
}

// Custom Hamcrest Matchers
public static Matcher<String> isValidEmail() {
    return matchesPattern("^[A-Za-z0-9+_.-]+@(.+)$");
}

public static Matcher<String> isValidUUID() {
    return matchesPattern("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
}
```

### **Bonus Challenges**
- Implement JSON Schema validation for all responses
- Add comprehensive error scenario testing
- Create data providers for parameterized testing
- Implement response caching for dependent tests

---

## ⚙️ Workshop 4: CI/CD Pipeline Configuration

### **Duration:** 10 minutes
### **Team Exercise (2-3 people)**
### **Difficulty:** Intermediate to Advanced

### **Scenario**
Configure a complete CI/CD pipeline for the test automation framework that supports multiple environments, parallel execution, and comprehensive reporting.

### **Requirements**
- Support for multiple Java versions (17, 21)
- Cross-browser testing (Chrome, Firefox, Safari)
- Parallel test execution
- Test result publishing and artifact management
- Integration with Slack for notifications
- Environment-specific configuration management

### **Implementation Tasks**

#### **Task 1: GitHub Actions Workflow (5 minutes)**
```yaml
name: Test Automation Pipeline
on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]
  schedule:
    - cron: '0 2 * * *'  # Nightly runs

env:
  JAVA_VERSION: '17'
  MAVEN_OPTS: '-Xmx1024m'

jobs:
  test:
    runs-on: ${{ matrix.os }}
    strategy:
      fail-fast: false
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
        java: [17, 21]
        browser: [chrome, firefox]
        # TODO: Add test-suite matrix dimension
        
    steps:
      - name: Checkout Repository
        uses: actions/checkout@v4
        
      - name: Setup Java
        uses: actions/setup-java@v4
        with:
          java-version: ${{ matrix.java }}
          distribution: 'temurin'
          cache: maven
          
      # TODO: Add browser setup steps
      # TODO: Add test execution steps
      # TODO: Add artifact publishing steps
```

#### **Task 2: Advanced Pipeline Features (3 minutes)**
```yaml
  # TODO: Implement these additional jobs
  
  security-scan:
    runs-on: ubuntu-latest
    steps:
      # TODO: Add dependency vulnerability scanning
      # TODO: Add SAST (Static Application Security Testing)
      
  performance-test:
    runs-on: ubuntu-latest
    needs: test
    if: github.ref == 'refs/heads/main'
    steps:
      # TODO: Add performance test execution
      # TODO: Add performance regression detection
      
  deploy-test-environment:
    runs-on: ubuntu-latest
    needs: [test, security-scan]
    if: github.ref == 'refs/heads/develop'
    steps:
      # TODO: Deploy to test environment
      # TODO: Run smoke tests against deployed environment
      
  notification:
    runs-on: ubuntu-latest
    needs: [test, security-scan, performance-test]
    if: always()
    steps:
      # TODO: Send Slack notification with results summary
```

#### **Task 3: Environment Configuration (2 minutes)**
```yaml
# .github/workflows/test-config.yml
# TODO: Create environment-specific configurations

# environments/dev.properties
browser=chrome
headless=true
base.url=https://dev.example.com
api.url=https://api-dev.example.com
timeout=30

# environments/staging.properties  
# TODO: Define staging environment properties

# environments/prod.properties
# TODO: Define production environment properties
```

### **Maven Integration**
```xml
<!-- TODO: Configure Surefire plugin for parallel execution -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.2</version>
    <configuration>
        <parallel>methods</parallel>
        <threadCount>4</threadCount>
        <systemPropertyVariables>
            <browser>${browser}</browser>
            <environment>${environment}</environment>
        </systemPropertyVariables>
    </configuration>
</plugin>

<!-- TODO: Add Failsafe plugin for integration tests -->
<!-- TODO: Add Allure reporting plugin -->
```

### **Docker Integration**
```dockerfile
# TODO: Create Dockerfile for test execution
FROM openjdk:17-jdk-slim

# Install browsers and dependencies
# TODO: Add browser installation steps

WORKDIR /app
COPY . .

RUN ./mvnw clean compile test-compile

# TODO: Define test execution command
ENTRYPOINT ["./mvnw", "test"]
```

### **Bonus Challenges**
- Add test result trend analysis
- Implement flaky test detection and quarantine
- Create custom GitHub Actions for test framework
- Add integration with external test management tools

---

## 🎭 Workshop 5: Advanced Mocking and Dependency Injection

### **Duration:** 10 minutes
### **Individual or Pair Exercise**
### **Difficulty:** Advanced

### **Scenario**
Create a comprehensive test suite for a complex service class that depends on multiple external services, using advanced Mockito features and dependency injection patterns.

### **Service Under Test**
```java
@Service
public class OrderProcessingService {
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final NotificationService notificationService;
    private final AuditService auditService;
    
    public OrderProcessingService(PaymentService paymentService,
                                InventoryService inventoryService,
                                NotificationService notificationService,
                                AuditService auditService) {
        // Constructor injection
    }
    
    public OrderResult processOrder(Order order) {
        // Complex business logic with multiple service interactions
        // TODO: Implement order processing workflow
    }
}
```

### **Implementation Tasks**

#### **Task 1: Mock Setup and Configuration (3 minutes)**
```java
@ExtendWith(MockitoExtension.class)
class OrderProcessingServiceTest {
    
    @Mock private PaymentService paymentService;
    @Mock private InventoryService inventoryService;
    @Mock private NotificationService notificationService;
    @Spy private AuditService auditService;
    
    @InjectMocks private OrderProcessingService orderProcessingService;
    
    @BeforeEach
    void setUp() {
        // TODO: Configure default mock behaviors
        // TODO: Set up common test data
    }
    
    // TODO: Create test data builders
    private Order createValidOrder() {
        return Order.builder()
                .customerId("CUST123")
                .items(Arrays.asList(
                    OrderItem.builder().productId("PROD001").quantity(2).build(),
                    OrderItem.builder().productId("PROD002").quantity(1).build()
                ))
                .build();
    }
}
```

#### **Task 2: Advanced Mocking Scenarios (4 minutes)**
```java
@Test
void testSuccessfulOrderProcessing() {
    // Arrange
    Order order = createValidOrder();
    
    // TODO: Configure mock behaviors for successful flow
    when(inventoryService.checkAvailability(any()))
        .thenReturn(InventoryResult.available());
    
    when(paymentService.processPayment(any(), any()))
        .thenReturn(PaymentResult.successful());
    
    // TODO: Configure notification service mock
    // TODO: Configure audit service spy behavior
    
    // Act
    OrderResult result = orderProcessingService.processOrder(order);
    
    // Assert
    assertThat(result.isSuccessful()).isTrue();
    
    // Verify interactions
    verify(inventoryService).reserveItems(order.getItems());
    verify(paymentService).processPayment(eq(order.getCustomerId()), any());
    verify(notificationService).sendOrderConfirmation(order.getCustomerId());
    
    // TODO: Add additional verifications
}

@Test
void testOrderProcessingWithPaymentFailure() {
    // TODO: Test payment failure scenario
    // Configure payment service to fail
    // Verify rollback operations are called
    // Verify error notifications are sent
}

@Test
void testOrderProcessingWithInventoryUnavailable() {
    // TODO: Test inventory unavailability scenario
    // Configure inventory service to return unavailable
    // Verify order is rejected appropriately
    // Verify no payment is attempted
}
```

#### **Task 3: Advanced Mockito Features (3 minutes)**
```java
@Test
void testOrderProcessingWithCustomMatchers() {
    // TODO: Use argument matchers for complex validations
    when(paymentService.processPayment(eq("CUST123"), argThat(amount -> 
        amount.compareTo(BigDecimal.valueOf(100)) > 0)))
        .thenReturn(PaymentResult.successful());
    
    // TODO: Use answer for dynamic responses
    when(inventoryService.checkAvailability(any())).thenAnswer(invocation -> {
        List<OrderItem> items = invocation.getArgument(0);
        // Dynamic logic based on input
        return InventoryResult.fromItems(items);
    });
}

@Test
void testServiceInteractionTiming() {
    // TODO: Verify call ordering
    InOrder inOrder = inOrder(inventoryService, paymentService, notificationService);
    
    orderProcessingService.processOrder(createValidOrder());
    
    inOrder.verify(inventoryService).checkAvailability(any());
    inOrder.verify(inventoryService).reserveItems(any());
    inOrder.verify(paymentService).processPayment(any(), any());
    inOrder.verify(notificationService).sendOrderConfirmation(any());
}

@Test
void testAsynchronousProcessing() {
    // TODO: Test async method calls
    // TODO: Use Mockito timeout verification
    verify(notificationService, timeout(5000)).sendOrderConfirmation(any());
}
```

### **Dependency Injection Integration**
```java
// Using Google Guice
public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PaymentService.class).toInstance(mock(PaymentService.class));
        bind(InventoryService.class).toInstance(mock(InventoryService.class));
        bind(NotificationService.class).toInstance(mock(NotificationService.class));
        bind(AuditService.class).toInstance(spy(new AuditServiceImpl()));
    }
}

// Test with Guice injection
@Test
void testWithGuiceInjection() {
    Injector injector = Guice.createInjector(new TestModule());
    OrderProcessingService service = injector.getInstance(OrderProcessingService.class);
    
    // TODO: Execute test with injected dependencies
}
```

### **Bonus Challenges**
- Implement BDD-style tests using Mockito-BDD
- Create custom argument matchers for domain objects
- Implement mock reset strategies for test isolation
- Add performance testing for service interactions

---

## 📝 Workshop Summary and Reflection

### **Key Learning Outcomes**
After completing these workshops, participants should have hands-on experience with:

1. **Architecture Design:** Creating scalable and maintainable framework architectures
2. **Page Objects:** Implementing robust and reusable page object patterns
3. **API Testing:** Building comprehensive REST-assured test suites
4. **CI/CD Integration:** Configuring complete automation pipelines
5. **Advanced Mocking:** Using sophisticated testing patterns for complex scenarios

### **Best Practices Reinforced**
- SOLID principles in test automation code
- Separation of concerns in framework design
- Proper use of design patterns (Page Object, Builder, Factory)
- Effective mock management and test isolation
- Comprehensive error handling and reporting

### **Next Steps for Participants**
1. Apply learned patterns to current projects
2. Refactor existing test code using new techniques
3. Implement CI/CD improvements in team workflows
4. Share knowledge with team members
5. Continue exploring advanced framework features

### **Additional Resources**
- Framework design pattern examples
- Advanced Mockito documentation
- CI/CD best practices guides
- Community forums and discussion groups