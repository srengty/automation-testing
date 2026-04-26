# Workshop Activities — Test Automation Tools

This document contains three hands-on workshop exercises covering Web UI, API, and Performance/Mobile testing tools.

---

## 🖥️ Workshop 1: Web UI Testing Tools

### **Duration:** 30 minutes
### **Group Size:** 2–3 participants
### **Difficulty:** Beginner to Intermediate

---

### Exercise A — Selenium WebDriver (15 minutes)

#### Scenario
Automate the login flow of a demo web application using Selenium WebDriver 4 and JUnit 5.

#### Setup (Maven `pom.xml` snippet)
```xml
<dependencies>
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.21.0</version>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### Tasks

1. **Setup (3 minutes)**
   - Create a Maven project with the Selenium 4 dependency
   - Note: Selenium Manager handles driver binaries automatically — no manual download needed

2. **Implement the Login Page Object (7 minutes)**
   ```java
   public class LoginPage {
       private final WebDriver driver;
       private final WebDriverWait wait;

       public LoginPage(WebDriver driver) {
           this.driver = driver;
           this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
       }

       public void login(String email, String password) {
           driver.get("https://the-internet.herokuapp.com/login");
           wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")))
               .sendKeys(email);
           driver.findElement(By.id("password")).sendKeys(password);
           driver.findElement(By.cssSelector("button[type='submit']")).click();
       }

       public String getFlashMessage() {
           return wait.until(ExpectedConditions.visibilityOfElementLocated(
               By.id("flash"))).getText();
       }
   }
   ```

3. **Write the Test (5 minutes)**
   - Write a `@Test` that asserts the flash message contains "You logged into a secure area" on valid credentials (`tomsmith` / `SuperSecretPassword!`)
   - Write a second `@Test` for invalid credentials asserting the error message
   - Close the driver in `@AfterEach`

#### Expected Outcome
- Both tests pass with clear assertion messages
- No `NoSuchElementException` due to missing waits

---

### Exercise B — Cypress (15 minutes)

#### Scenario
Write a Cypress E2E test for the login flow using `cy.intercept()` to stub the API response.

#### Setup
```bash
mkdir cypress-demo && cd cypress-demo
npm init -y
npm install --save-dev cypress
npx cypress open   # First launch — creates folder structure
```

#### Tasks

1. **Create the test file** at `cypress/e2e/login.cy.js`

2. **Implement with network stubbing (10 minutes)**
   ```javascript
   describe('Login', () => {
     it('succeeds with valid credentials (stubbed)', () => {
       cy.intercept('POST', '/api/auth/login', {
         statusCode: 200,
         body: { token: 'fake-jwt-token', user: { name: 'Alice' } }
       }).as('loginRequest');

       cy.visit('https://example.cypress.io');   // Replace with your app URL
       cy.get('[data-cy="email"]').type('alice@example.com');
       cy.get('[data-cy="password"]').type('Secret@123');
       cy.get('[data-cy="login-btn"]').click();

       cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
     });

     it('shows error on invalid credentials', () => {
       cy.intercept('POST', '/api/auth/login', { statusCode: 401 }).as('failedLogin');
       cy.visit('/login');
       cy.get('[data-cy="email"]').type('bad@user.com');
       cy.get('[data-cy="password"]').type('wrongpass');
       cy.get('[data-cy="login-btn"]').click();
       cy.get('[data-cy="error-message"]').should('be.visible');
     });
   });
   ```

3. **Run headlessly (5 minutes)**
   ```bash
   npx cypress run --spec "cypress/e2e/login.cy.js"
   ```

#### Expected Outcome
- Tests run successfully in headless mode
- Screenshots saved to `cypress/screenshots/` on failure
- Video recording saved to `cypress/videos/`

#### Discussion
- What is the advantage of using `cy.intercept()` over a real API call in E2E tests?
- When would you NOT stub API calls?

---

## 🔌 Workshop 2: API Testing Tools

### **Duration:** 30 minutes
### **Group Size:** 2–3 participants
### **Difficulty:** Beginner to Intermediate

---

### Exercise A — Postman + Newman (15 minutes)

#### Scenario
Create a Postman collection to test the public `reqres.in` REST API, then run it via Newman in CI mode.

#### Target API
Base URL: `https://reqres.in`

#### Tasks

1. **Create Collection in Postman (5 minutes)**
   - Create a new Collection: `ReqRes API Tests`
   - Set a Collection variable: `{{baseUrl}}` = `https://reqres.in`

2. **Add Requests with Tests (8 minutes)**

   **Request 1: GET /api/users**
   ```javascript
   // Tests tab
   pm.test("Status 200", () => pm.response.to.have.status(200));
   pm.test("Returns user list", () => {
       const json = pm.response.json();
       pm.expect(json.data).to.be.an('array').with.length.greaterThan(0);
       pm.expect(json.data[0]).to.have.property('email');
   });
   pm.collectionVariables.set("userId", pm.response.json().data[0].id);
   ```

   **Request 2: POST /api/users**
   ```javascript
   // Body (raw JSON):
   { "name": "Test User", "job": "QA Engineer" }

   // Tests tab:
   pm.test("Status 201", () => pm.response.to.have.status(201));
   pm.test("User created with name", () => {
       pm.expect(pm.response.json().name).to.eql("Test User");
   });
   pm.test("Response time < 500ms", () => pm.expect(pm.response.responseTime).to.be.below(500));
   ```

   **Request 3: DELETE /api/users/2**
   ```javascript
   // Tests tab:
   pm.test("Status 204 No Content", () => pm.response.to.have.status(204));
   ```

3. **Export and Run with Newman (2 minutes)**
   ```bash
   # Export collection as reqres-tests.json
   npm install -g newman newman-reporter-htmlextra
   newman run reqres-tests.json --reporters cli,htmlextra
   ```

#### Expected Outcome
- All 5 test assertions pass
- HTML report generated with test summary

---

### Exercise B — REST Assured (15 minutes)

#### Scenario
Write parameterised REST Assured tests for the same `reqres.in` API.

#### Setup (Maven)
```xml
<dependencies>
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>5.4.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

#### Tasks

1. **Create base test class (3 minutes)**
   ```java
   public abstract class BaseAPITest {
       protected static final String BASE_URL = "https://reqres.in";

       @BeforeAll
       static void configure() {
           RestAssured.baseURI = BASE_URL;
           RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
       }
   }
   ```

2. **Write test methods (10 minutes)**
   ```java
   class UserAPITest extends BaseAPITest {

       @Test
       void getUsersReturnsPagedList() {
           given().queryParam("page", 1)
           .when().get("/api/users")
           .then()
               .statusCode(200)
               .body("page", equalTo(1))
               .body("data", hasSize(greaterThan(0)))
               .body("data[0].email", containsString("@"));
       }

       @Test
       void createUserReturns201() {
           String body = """{ "name": "Alice", "job": "QA Lead" }""";
           given().contentType(ContentType.JSON).body(body)
           .when().post("/api/users")
           .then()
               .statusCode(201)
               .body("name", equalTo("Alice"))
               .body("id", notNullValue());
       }

       @ParameterizedTest
       @ValueSource(ints = {1, 2, 3})
       void getSingleUserReturns200(int userId) {
           given()
           .when().get("/api/users/" + userId)
           .then().statusCode(200)
               .body("data.id", equalTo(userId));
       }
   }
   ```

3. **Run the tests (2 minutes)**
   ```bash
   mvn test -Dtest=UserAPITest
   ```

#### Expected Outcome
- All tests pass with clear assertion failure messages
- Request/response logs appear on failure
- Parameterised test runs 3 times (userId 1, 2, 3)

---

## ⚡ Workshop 3: Performance and Mobile Testing

### **Duration:** 40 minutes
### **Group Size:** 2–3 participants
### **Difficulty:** Intermediate

---

### Exercise A — Apache JMeter (20 minutes)

#### Scenario
Build a JMeter load test for the `reqres.in` API simulating 50 concurrent users.

#### Tasks

1. **Create Test Plan structure (5 minutes)**
   - Open JMeter GUI
   - Add **Thread Group**: 50 threads, ramp-up 30s, loop count 3
   - Add **HTTP Request Defaults**: Server `reqres.in`, Protocol `https`, Port 443

2. **Add samplers and assertions (10 minutes)**
   - **HTTP Sampler 1**: GET `/api/users?page=1`
     - Add **Response Assertion**: Response code = 200
     - Add **Duration Assertion**: Max response time 2000ms
   - **HTTP Sampler 2**: POST `/api/users` with JSON body `{"name":"LoadUser","job":"tester"}`
     - Add **JSON Assertion**: `$.name` = `LoadUser`
   - Add **CSV Data Set Config** to parameterise user names from a CSV file

3. **Run and generate report (5 minutes)**
   ```bash
   # Save as reqres-load-test.jmx, then run headlessly:
   jmeter -n -t reqres-load-test.jmx -l results.jtl -e -o jmeter-report/
   ```
   - Open `jmeter-report/index.html` in a browser
   - Note: 90th percentile response time, error rate, and throughput (req/sec)

#### Expected Outcome
- Error rate < 1%
- 90th percentile response time < 500ms
- HTML dashboard report generated

---

### Exercise B — Flutter Widget Test (20 minutes)

#### Scenario
Write widget tests for a simple counter application.

#### Setup
```bash
flutter create counter_app
cd counter_app
flutter test   # Verify default test passes
```

#### Tasks

1. **Review the existing `main.dart` counter widget (2 minutes)**
   - Understand `MyHomePage` with increment and decrement buttons

2. **Write widget tests in `test/widget_test.dart` (13 minutes)**
   ```dart
   import 'package:flutter/material.dart';
   import 'package:flutter_test/flutter_test.dart';
   import 'package:counter_app/main.dart';

   void main() {
     group('Counter Widget Tests', () {
       testWidgets('initial counter value is 0', (WidgetTester tester) async {
         await tester.pumpWidget(const MyApp());
         expect(find.text('0'), findsOneWidget);
         expect(find.text('1'), findsNothing);
       });

       testWidgets('increment increases counter', (WidgetTester tester) async {
         await tester.pumpWidget(const MyApp());
         await tester.tap(find.byIcon(Icons.add));
         await tester.pump();
         expect(find.text('1'), findsOneWidget);
       });

       testWidgets('multiple increments accumulate', (WidgetTester tester) async {
         await tester.pumpWidget(const MyApp());
         for (int i = 0; i < 5; i++) {
           await tester.tap(find.byIcon(Icons.add));
           await tester.pump();
         }
         expect(find.text('5'), findsOneWidget);
       });

       testWidgets('counter does not go below zero', (WidgetTester tester) async {
         await tester.pumpWidget(const MyApp());
         await tester.tap(find.byIcon(Icons.remove));
         await tester.pump();
         expect(find.text('0'), findsOneWidget);   // Still 0, not -1
       });
     });
   }
   ```

3. **Run all tests (5 minutes)**
   ```bash
   flutter test --reporter expanded
   ```

#### Expected Outcome
- All 4 widget tests pass
- Counter boundary condition (no negative values) is validated
- Tests run in under 5 seconds without a device

#### Discussion
- What is the difference between `pump()` and `pumpAndSettle()`?
- When would you use `tester.tap()` vs `tester.tapAt()`?

---

## 📋 General Workshop Guidelines

### Time Management
- Spend at most 2 minutes troubleshooting environment issues before asking the instructor
- If setup fails, pair with a working group to observe and participate
- Focus on understanding the concepts; complete solutions are provided in Practical-Examples.md

### Evaluation Criteria
| Criterion | Weight |
|-----------|--------|
| Working test implementation | 50% |
| Correct assertions and validations | 25% |
| Code clarity and structure | 15% |
| Discussion contribution | 10% |

### Submission
- Commit your code to a personal branch: `workshop04/your-name`
- Share the Newman HTML report and JMeter dashboard links with the instructor
