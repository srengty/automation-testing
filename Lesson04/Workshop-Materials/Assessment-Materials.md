# Assessment Materials — Test Automation Tools

## 🎯 Learning Outcome Alignment

| Learning Outcome | Assessment Method | Weight |
|-----------------|------------------|--------|
| Select appropriate tools for test types | Discussion Q1 | 15% |
| Write Web UI tests (Selenium/Cypress) | Workshop 1 | 25% |
| Write API tests (Postman/REST Assured) | Workshop 2 | 25% |
| Build performance tests (JMeter/Gatling) | Workshop 3A | 20% |
| Write Flutter mobile tests | Workshop 3B | 15% |

---

## 📝 Knowledge Check Quiz

### Section 1: Web UI Testing (10 points)

**Q1.** Which statement best describes the key difference between Selenium and Cypress?  
a) Selenium only supports Chrome; Cypress supports all browsers  
b) Cypress runs inside the browser and has auto-retry; Selenium uses the external WebDriver protocol  
c) Selenium is faster than Cypress because it does not require Node.js  
d) Cypress requires a WebDriver installation; Selenium does not  

**Answer: B** ✓

---

**Q2.** In Selenium WebDriver, which wait strategy is recommended for production test suites?  
a) Implicit wait — set once and applies to all elements  
b) `Thread.sleep()` — always reliable  
c) Explicit wait with `WebDriverWait` and `ExpectedConditions`  
d) No wait — all elements are immediately available  

**Answer: C** ✓

---

**Q3.** What does `cy.intercept('POST', '/api/login', {...})` do in Cypress?  
a) Sends a real POST request to `/api/login`  
b) Blocks all POST requests permanently  
c) Stubs the matching HTTP request and returns a mocked response  
d) Records the request for later playback  

**Answer: C** ✓

---

**Q4.** Which Puppeteer feature allows it to block image downloads to speed up tests?  
a) `page.setViewport()`  
b) `page.setRequestInterception(true)` and aborting requests by resource type  
c) `browser.newContext()` with media disabled  
d) `page.goto()` with `waitUntil: 'networkidle0'`  

**Answer: B** ✓

---

### Section 2: API Testing (10 points)

**Q5.** What is the purpose of Postman Environments?  
a) To organise requests into folders  
b) To store variables (like base URLs and tokens) that differ between dev, staging, and production  
c) To schedule collection runs automatically  
d) To generate API documentation  

**Answer: B** ✓

---

**Q6.** In REST Assured, what does `.body("data[0].email", containsString("@"))` verify?  
a) The first element's email field contains the "@" character  
b) The entire response body contains an "@" character  
c) The request body has an email field  
d) Nothing — `containsString` is not a valid Hamcrest matcher  

**Answer: A** ✓

---

**Q7.** Which Newman command generates an HTML report from a Postman collection?  
a) `newman run collection.json --html report.html`  
b) `newman run collection.json --reporters htmlextra`  
c) `newman export report.html`  
d) `postman run collection.json --output html`  

**Answer: B** ✓

---

**Q8.** What is the main advantage of REST Assured over Postman for automated regression suites?  
a) REST Assured supports more HTTP methods  
b) REST Assured has a GUI for writing tests  
c) REST Assured tests are code — they are version-controlled, peer-reviewed, and run natively with Maven/Gradle  
d) Postman cannot test authenticated endpoints  

**Answer: C** ✓

---

### Section 3: Performance Testing (10 points)

**Q9.** In JMeter, what does a "Thread Group" represent?  
a) A single HTTP request  
b) A set of concurrent virtual users and their execution parameters  
c) A group of assertions on a single response  
d) A collection of JMeter plugins  

**Answer: B** ✓

---

**Q10.** Which Gatling injection profile ramps users up gradually over a given duration?  
a) `constantUsersPerSec(10).during(60)`  
b) `atOnceUsers(100)`  
c) `rampUsers(50).during(30)`  
d) `heavisideUsers(100).during(60)`  

**Answer: C** ✓

---

**Q11.** Why is running JMeter in CLI (non-GUI) mode recommended for actual load tests?  
a) The GUI does not support assertions  
b) The GUI consumes significant resources, which skews load test results  
c) CLI mode generates HTML reports; GUI mode does not  
d) CLI mode runs faster because it uses fewer threads  

**Answer: B** ✓

---

**Q12.** What is the primary advantage of Gatling over JMeter for a team that uses Git for everything?  
a) Gatling supports more protocols  
b) Gatling simulations are code (Java/Scala) — they are version-controlled and diff-able like any source file  
c) Gatling runs on fewer resources  
d) Gatling produces better charts  

**Answer: B** ✓

---

### Section 4: Flutter Mobile Testing (10 points)

**Q13.** What does `await tester.pumpAndSettle()` do in a Flutter widget test?  
a) Pumps exactly one animation frame  
b) Waits for all animations and microtasks to complete before proceeding  
c) Restarts the Flutter engine  
d) Clears the widget tree  

**Answer: B** ✓

---

**Q14.** Which Flutter test type requires a physical device or emulator?  
a) Unit tests  
b) Widget tests  
c) Integration tests  
d) All of the above  

**Answer: C** ✓

---

**Q15.** What capability does the Patrol package add to Flutter integration tests?  
a) Faster widget rendering in test mode  
b) Interaction with native OS elements such as permission dialogs and notifications  
c) Visual regression screenshot comparison  
d) Network request recording and playback  

**Answer: B** ✓

---

**Q16.** What command runs all Flutter tests in a project including integration tests on a connected device?  
a) `flutter build test`  
b) `flutter run --test`  
c) `flutter test integration_test/ -d <device-id>`  
d) `dart test --integration`  

**Answer: C** ✓

---

## 📊 Workshop Evaluation Rubrics

### Workshop 1 — Web UI Testing

| Criterion | Excellent (4) | Good (3) | Adequate (2) | Needs Work (1) |
|-----------|---------------|----------|--------------|----------------|
| Tests run without errors | All tests pass | Minor warnings | Some failures | Does not compile |
| Assertion quality | Specific, meaningful assertions | Good assertions | Basic assertions | No assertions |
| Wait strategy | Explicit waits used correctly | Mostly correct | Implicit only | No waits |
| Code structure | Page Object used, clean code | Partial POM | Some structure | No structure |

### Workshop 2 — API Testing

| Criterion | Excellent (4) | Good (3) | Adequate (2) | Needs Work (1) |
|-----------|---------------|----------|--------------|----------------|
| Collection/test completeness | All 3 CRUD operations tested | 2 operations | 1 operation | None |
| Test script quality | Validates status + body + time | Status + body | Status only | No scripts |
| Environment variables | Used correctly throughout | Some usage | Hardcoded values | No variables |
| Newman report | Generated and shared | Generated | Partially generated | Not attempted |

### Workshop 3 — Performance + Mobile

| Criterion | Excellent (4) | Good (3) | Adequate (2) | Needs Work (1) |
|-----------|---------------|----------|--------------|----------------|
| JMeter plan completeness | Thread group + assertions + CLI run | Thread group + run | Thread group only | Not created |
| Performance report | Dashboard generated and analysed | Generated | Partial | Not generated |
| Flutter tests completeness | All 4 tests + boundary case | 3 tests | 2 tests | 1 test |
| Flutter test quality | Clear assertions, `pump()` used | Some assertions | Runs but weak | Does not run |

---

## 💡 Reflection Questions (Take-home)

1. For a project with a Java backend, a React frontend, and Flutter mobile app — which combination of the tools covered today would you recommend and why?

2. Your API test suite takes 45 minutes to run. What strategies can you apply using the tools covered today to reduce feedback time?

3. Describe a scenario where you would use JMeter instead of Gatling, and vice versa.

4. What is the Flutter testing equivalent of Selenium's Page Object Model pattern?

5. How would you detect a performance regression between two releases using the tools covered today?
