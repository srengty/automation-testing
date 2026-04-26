# Practical Examples — Test Automation Tools

This document contains complete, working examples for each tool covered in Lesson04.

---

## 🖥️ Web UI Testing

### Selenium WebDriver — Full Login Test with POM

```java
// src/test/java/ui/LoginPageTest.java
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

class LoginPageTest {
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.manage().window().setSize(new Dimension(1280, 800));
    }

    @Test
    void validLoginSucceeds() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.enterCredentials("tomsmith", "SuperSecretPassword!");
        loginPage.submit();
        Assertions.assertTrue(loginPage.getFlashMessage().contains("You logged into"));
    }

    @Test
    void invalidLoginShowsError() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        loginPage.enterCredentials("invalid", "wrong");
        loginPage.submit();
        Assertions.assertTrue(loginPage.getFlashMessage().contains("Your username is invalid"));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }
}

// Page Object
class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final String URL = "https://the-internet.herokuapp.com/login";

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() { driver.get(URL); }

    public void enterCredentials(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")))
            .sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
    }

    public void submit() {
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    public String getFlashMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("flash"))).getText();
    }
}
```

---

### Cypress — Component and E2E Tests

```javascript
// cypress/e2e/shopping-cart.cy.js
describe('Shopping Cart', () => {
  beforeEach(() => {
    // Stub product API
    cy.intercept('GET', '/api/products', { fixture: 'products.json' }).as('getProducts');
    cy.visit('/shop');
    cy.wait('@getProducts');
  });

  it('adds product to cart', () => {
    cy.get('[data-cy="product-card"]').first().within(() => {
      cy.get('[data-cy="add-to-cart"]').click();
    });
    cy.get('[data-cy="cart-count"]').should('contain', '1');
  });

  it('removes product from cart', () => {
    cy.get('[data-cy="add-to-cart"]').first().click();
    cy.get('[data-cy="cart-icon"]').click();
    cy.get('[data-cy="remove-item"]').first().click();
    cy.get('[data-cy="cart-empty"]').should('be.visible');
  });

  it('checkout flow completes', () => {
    cy.intercept('POST', '/api/orders', { statusCode: 201, body: { orderId: 'ORD-001' } })
      .as('placeOrder');
    cy.get('[data-cy="add-to-cart"]').first().click();
    cy.get('[data-cy="checkout-btn"]').click();
    cy.wait('@placeOrder');
    cy.url().should('include', '/order-confirmation');
    cy.get('[data-cy="order-id"]').should('contain', 'ORD-001');
  });
});

// cypress/fixtures/products.json
// [{"id":1,"name":"Widget A","price":9.99},{"id":2,"name":"Widget B","price":14.99}]
```

---

### Puppeteer — Performance Audit Script

```javascript
// scripts/audit.js
const puppeteer = require('puppeteer');

(async () => {
  const browser = await puppeteer.launch({ headless: 'new' });
  const page = await browser.newPage();

  // Enable performance metrics
  await page.setCacheEnabled(false);
  const client = await page.target().createCDPSession();
  await client.send('Performance.enable');

  await page.goto('https://example.com', { waitUntil: 'networkidle2' });

  // Capture performance metrics
  const metrics = await page.metrics();
  console.log('JS Heap Used:', (metrics.JSHeapUsedSize / 1024 / 1024).toFixed(2), 'MB');
  console.log('Layout Count:', metrics.LayoutCount);
  console.log('Script Duration:', metrics.ScriptDuration.toFixed(3), 's');

  // Full page screenshot
  await page.screenshot({ path: 'screenshots/full-page.png', fullPage: true });

  // Generate PDF report
  await page.pdf({ path: 'reports/page.pdf', format: 'A4', printBackground: true });

  await browser.close();
})();
```

---

## 🔌 API Testing

### REST Assured — Full Test Suite with Auth

```java
// src/test/java/api/UserAPITest.java
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class UserAPITest {
    private static String authToken;
    private static RequestSpecification authSpec;

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Authenticate and store token
        authToken = given()
            .contentType(ContentType.JSON)
            .body("""{"email": "eve.holt@reqres.in", "password": "cityslicka"}""")
        .when()
            .post("/api/login")
        .then()
            .statusCode(200)
            .extract().path("token");

        authSpec = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + authToken);
    }

    @Test
    void getUsersReturnsPaginatedList() {
        given().queryParam("page", 1)
        .when().get("/api/users")
        .then()
            .statusCode(200)
            .body("page", equalTo(1))
            .body("total_pages", greaterThan(0))
            .body("data", hasSize(greaterThan(0)))
            .body("data.email", everyItem(containsString("@")));
    }

    @Test
    void createUserReturnsCreatedUser() {
        String requestBody = """{"name": "Test User", "job": "QA Automation Engineer"}""";

        Response response = given()
            .spec(authSpec)
            .body(requestBody)
        .when()
            .post("/api/users")
        .then()
            .statusCode(201)
            .body("name", equalTo("Test User"))
            .body("job", equalTo("QA Automation Engineer"))
            .body("id", notNullValue())
            .extract().response();

        // Extract and validate createdAt timestamp
        String createdAt = response.jsonPath().getString("createdAt");
        Assertions.assertNotNull(createdAt);
        Assertions.assertTrue(createdAt.contains("2024") || createdAt.contains("2025") || createdAt.contains("2026"));
    }

    @Test
    void updateUserReturns200() {
        given()
            .spec(authSpec)
            .body("""{"name": "Updated Name", "job": "Senior QA"}""")
        .when()
            .put("/api/users/2")
        .then()
            .statusCode(200)
            .body("name", equalTo("Updated Name"))
            .body("updatedAt", notNullValue());
    }

    @Test
    void deleteUserReturns204() {
        given()
            .spec(authSpec)
        .when()
            .delete("/api/users/2")
        .then()
            .statusCode(204);
    }

    @Test
    void nonExistentUserReturns404() {
        when().get("/api/users/9999")
        .then().statusCode(404);
    }
}
```

---

### Postman — Pre-request Script with Dynamic Data

```javascript
// Collection pre-request script — generate unique test data
const timestamp = Date.now();
pm.collectionVariables.set("testEmail", `user_${timestamp}@test.com`);
pm.collectionVariables.set("testName", `Test User ${timestamp}`);

// Auto-refresh auth token if expired
const tokenExpiry = pm.collectionVariables.get("tokenExpiry");
if (!tokenExpiry || Date.now() > parseInt(tokenExpiry)) {
    pm.sendRequest({
        url: pm.collectionVariables.get("baseUrl") + "/api/login",
        method: "POST",
        header: { "Content-Type": "application/json" },
        body: {
            mode: "raw",
            raw: JSON.stringify({
                email: pm.environment.get("adminEmail"),
                password: pm.environment.get("adminPassword")
            })
        }
    }, (err, res) => {
        if (!err && res.code === 200) {
            pm.collectionVariables.set("authToken", res.json().token);
            pm.collectionVariables.set("tokenExpiry", Date.now() + 3600000); // 1 hour
        }
    });
}
```

---

## ⚡ Performance Testing

### Gatling — Complete Simulation with Scenarios

```java
// src/test/java/performance/ECommerceSimulation.java
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ECommerceSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
        .baseUrl("https://api.example.com")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json")
        .userAgentHeader("Gatling/3.10 LoadTest");

    // Feeder for parameterised test data
    FeederBuilder<String> userFeeder = csv("users.csv").circular();

    ChainBuilder browseProducts = exec(
        http("List Products").get("/products")
            .check(status().is(200), jsonPath("$[0].id").saveAs("productId"))
    ).pause(1, 3);

    ChainBuilder viewProduct = exec(
        http("View Product").get("/products/#{productId}")
            .check(status().is(200), jsonPath("$.name").exists())
    ).pause(2, 5);

    ChainBuilder addToCart = exec(
        http("Add to Cart").post("/cart/items")
            .body(StringBody("""{"productId": "#{productId}", "quantity": 1}"""))
            .check(status().is(201))
    );

    ChainBuilder checkout = feed(userFeeder)
        .exec(
            http("Checkout").post("/orders")
                .body(StringBody("""{"userId": "#{userId}", "paymentMethod": "card"}"""))
                .check(status().is(201), jsonPath("$.orderId").saveAs("orderId"))
        )
        .pause(1);

    ScenarioBuilder browseScenario = scenario("Browse and Buy")
        .exec(browseProducts, viewProduct, addToCart, checkout);

    ScenarioBuilder browseOnlyScenario = scenario("Browse Only")
        .repeat(5).on(exec(browseProducts, viewProduct));

    {
        setUp(
            browseScenario.injectOpen(
                rampUsers(20).during(30),
                constantUsersPerSec(5).during(120)
            ),
            browseOnlyScenario.injectOpen(
                rampUsers(50).during(30)
            )
        )
        .protocols(httpProtocol)
        .assertions(
            global().responseTime().percentile3().lt(1000),   // 99th percentile < 1s
            global().successfulRequests().percent().gt(99.0),  // Error rate < 1%
            forAll().responseTime().mean().lt(500)             // Mean < 500ms everywhere
        );
    }
}
```

---

### JMeter — Parameterised Test Plan (XML snippet)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2" properties="5.0">
  <hashTree>
    <TestPlan guiclass="TestPlanGui" testclass="TestPlan" testname="API Load Test">
      <elementProp name="TestPlan.user_defined_variables" elementType="Arguments">
        <collectionProp name="Arguments.arguments">
          <elementProp name="BASE_URL" elementType="Argument">
            <stringProp name="Argument.name">BASE_URL</stringProp>
            <stringProp name="Argument.value">reqres.in</stringProp>
          </elementProp>
        </collectionProp>
      </elementProp>
    </TestPlan>
    <hashTree>
      <ThreadGroup testname="50 Concurrent Users">
        <intProp name="ThreadGroup.num_threads">50</intProp>
        <intProp name="ThreadGroup.ramp_time">30</intProp>
        <intProp name="ThreadGroup.duration">120</intProp>
        <boolProp name="ThreadGroup.scheduler">true</boolProp>
      </ThreadGroup>
      <!-- HTTP Sampler, Assertions, Listeners follow -->
    </hashTree>
  </hashTree>
</jmeterTestPlan>
```

---

## 📱 Flutter Mobile Testing

### Complete Widget Test Suite

```dart
// test/widget/counter_test.dart
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:counter_app/main.dart';

void main() {
  group('Counter App Widget Tests', () {
    late Widget app;

    setUp(() {
      app = const MaterialApp(home: MyHomePage(title: 'Counter'));
    });

    testWidgets('renders initial state correctly', (tester) async {
      await tester.pumpWidget(app);

      expect(find.text('Counter'), findsOneWidget);
      expect(find.text('0'), findsOneWidget);
      expect(find.byIcon(Icons.add), findsOneWidget);
      expect(find.byIcon(Icons.remove), findsOneWidget);
    });

    testWidgets('increment button increases count', (tester) async {
      await tester.pumpWidget(app);

      await tester.tap(find.byIcon(Icons.add));
      await tester.pump();

      expect(find.text('1'), findsOneWidget);
    });

    testWidgets('decrement button decreases count', (tester) async {
      await tester.pumpWidget(app);

      // First increment to 2
      await tester.tap(find.byIcon(Icons.add));
      await tester.tap(find.byIcon(Icons.add));
      await tester.pump();
      expect(find.text('2'), findsOneWidget);

      // Then decrement
      await tester.tap(find.byIcon(Icons.remove));
      await tester.pump();
      expect(find.text('1'), findsOneWidget);
    });

    testWidgets('counter does not go below 0', (tester) async {
      await tester.pumpWidget(app);
      await tester.tap(find.byIcon(Icons.remove));
      await tester.pump();
      expect(find.text('0'), findsOneWidget);
      expect(find.text('-1'), findsNothing);
    });

    testWidgets('counter resets on long-press', (tester) async {
      await tester.pumpWidget(app);
      for (int i = 0; i < 10; i++) {
        await tester.tap(find.byIcon(Icons.add));
      }
      await tester.pump();
      expect(find.text('10'), findsOneWidget);

      await tester.longPress(find.text('10'));
      await tester.pump();
      expect(find.text('0'), findsOneWidget);
    });
  });
}
```

### Integration Test with Patrol

```dart
// integration_test/app_test.dart
import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';
import 'package:patrol/patrol.dart';
import 'package:my_app/main.dart' as app;

void main() {
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();

  patrolTest('full onboarding with notification permission', (PatrolIntegrationTester $) async {
    app.main();
    await $.pumpAndSettle();

    // App-level interactions
    await $.tap(find.byKey(const Key('get-started-btn')));
    await $.pumpAndSettle();

    // Native system permission dialog — grant notifications
    if (await $.native.isPermissionDialogVisible(timeout: const Duration(seconds: 5))) {
      await $.native.grantPermissionWhenInUse();
    }

    // Verify onboarding completes
    await $.tap(find.byKey(const Key('skip-tour-btn')));
    await $.pumpAndSettle();
    expect(find.byType(HomeScreen), findsOneWidget);
  });
}
```
