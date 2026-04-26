# Lesson04: Test Automation Tools — Complete Session Package

## 🎯 Session Overview

**Duration:** 6 hours (360 minutes)  
**Format:** Interactive presentation + hands-on workshops + comprehensive labs  
**Audience:** Test automation engineers, developers, QA professionals  

---

## 📁 Complete Package Contents

### **Core Presentation**
- **[Test Automation Tools.html](Test%20Automation%20Tools.html)** — 34-slide interactive presentation
  - Professional styling with keyboard navigation
  - Code examples for all major tools
  - Progress tracking and image lightboxes
  - Mobile-responsive design

### **Hands-on Workshops**
- **[Workshop-Activities.md](Workshop-Materials/Workshop-Activities.md)** — 3 practical workshop exercises
  - Web UI automation (Selenium + Cypress)
  - API testing (Postman + REST Assured)
  - Performance & Mobile (JMeter + Flutter)

### **Instructor Materials**
- **[Instructor-Guide.md](Workshop-Materials/Instructor-Guide.md)** — Complete session plan and facilitation notes
- **[Assessment-Materials.md](Workshop-Materials/Assessment-Materials.md)** — Evaluation rubrics and quizzes
- **[Practical-Examples.md](Workshop-Materials/Practical-Examples.md)** — Real-world tool usage examples
- **[Tool-Setup-Guide.md](Workshop-Materials/Tool-Setup-Guide.md)** — Environment configuration for all tools

---

## ⏰ Session Timeline

### **Part 1: Foundations and Tool Landscape (35 minutes)**
- **0:00–0:05:** Welcome and environment setup verification
- **0:05–0:10:** Agenda and learning objectives
- **0:10–0:25:** Test automation tool landscape and categories
- **0:25–0:35:** Tool selection criteria

### **Part 2: Web UI Testing Tools (110 minutes)**
- **0:35–0:50:** Selenium WebDriver — introduction and cross-browser support
- **0:50–1:05:** Selenium — setup, selectors, and wait strategies
- **1:05–1:15:** Selenium — architecture deep dive
- **1:15–1:30:** Cypress — in-browser architecture and features
- **1:30–1:45:** Cypress — writing tests, cy.intercept(), CLI
- **1:45–2:00:** Puppeteer — DevTools Protocol, headless scripting
- **2:00–2:15:** Puppeteer — code examples and network control
- **2:15–2:25:** Web UI tools comparison table
- **2:25–2:55:** **Workshop 1** — Selenium + Cypress hands-on (30 min)

### **Part 3: API Testing Tools (100 minutes)**
- **2:55–3:10:** Postman — collections, environments, and monitors
- **3:10–3:25:** Postman — test scripts and Newman CLI
- **3:25–3:40:** REST Assured — BDD syntax and JSON Path
- **3:40–3:55:** REST Assured — writing tests with Hamcrest matchers
- **3:55–4:05:** API tools comparison
- **4:05–4:35:** **Workshop 2** — Postman + REST Assured hands-on (30 min)

### **Part 4: Performance Testing Tools (90 minutes)**
- **4:35–4:50:** Apache JMeter — introduction and components
- **4:50–5:05:** JMeter — test plan structure and CLI execution
- **5:05–5:20:** Gatling — code-first load testing introduction
- **5:20–5:35:** Gatling — simulation scripts and injection profiles
- **5:35–5:45:** Performance tools comparison

### **Part 5: Flutter Mobile Testing (55 minutes)**
- **5:45–5:55:** Flutter testing pyramid overview
- **5:55–6:10:** Widget tests with flutter_test
- **6:10–6:25:** Integration tests on real devices
- **6:25–6:40:** Patrol — native interactions and permissions
- **6:40–7:20:** **Workshop 3** — JMeter + Flutter hands-on (40 min)

### **Wrap-up and Assessment (40 minutes)**
- **7:20–7:35:** CI/CD pipeline integration for all tools
- **7:35–7:45:** Best practices and tool selection principles
- **7:45–7:55:** Knowledge assessment and discussion
- **7:55–8:00:** Summary and next steps

---

## 🛠️ Prerequisites and Setup

### **Required Software**

#### Web UI Testing
- **Chrome browser** (latest) — for Selenium and Puppeteer
- **Java 17+** and **Maven 3.8+** — for Selenium (Java bindings)
- **Node.js 18+** and **npm** — for Cypress and Puppeteer

```bash
# Verify installations
java -version
mvn -version
node -version
npm -version
```

#### API Testing
- **Postman** desktop app — https://www.postman.com/downloads/
- **Newman** CLI:
  ```bash
  npm install -g newman newman-reporter-htmlextra
  ```
- **Java 17+** and **Maven** — for REST Assured

#### Performance Testing
- **Apache JMeter 5.6+** — https://jmeter.apache.org/download_jmeter.cgi
  ```bash
  # Run JMeter GUI
  jmeter.sh   # macOS/Linux
  jmeter.bat  # Windows
  ```
- **Java 17+** and **Maven** — for Gatling Java DSL

#### Flutter Mobile Testing
- **Flutter SDK 3.x** — https://docs.flutter.dev/get-started/install
- **Android Studio** with Android emulator or Xcode for iOS simulator
  ```bash
  flutter doctor   # Verify setup
  flutter test     # Run widget tests
  ```

---

## 📦 Maven Dependencies Reference

### Selenium WebDriver
```xml
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>4.21.0</version>
</dependency>
```

### REST Assured
```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.4.0</version>
    <scope>test</scope>
</dependency>
```

### Gatling (Java DSL)
```xml
<dependency>
    <groupId>io.gatling</groupId>
    <artifactId>gatling-app</artifactId>
    <version>3.10.5</version>
</dependency>
<plugin>
    <groupId>io.gatling</groupId>
    <artifactId>gatling-maven-plugin</artifactId>
    <version>4.9.6</version>
</plugin>
```

### Flutter (pubspec.yaml)
```yaml
dev_dependencies:
  flutter_test:
    sdk: flutter
  integration_test:
    sdk: flutter
  patrol: ^3.0.0
```

---

## 🔑 Key Concepts Reference

| Tool | Category | Language | Key Feature |
|------|----------|----------|-------------|
| Selenium | Web UI | Java/Python/C#/JS | Cross-browser, W3C standard |
| Cypress | Web UI | JavaScript | In-browser, auto-retry |
| Puppeteer | Web UI | JavaScript | DevTools Protocol |
| Postman | API | JavaScript (scripts) | GUI + Newman CLI |
| REST Assured | API | Java | BDD DSL, JSON Path |
| JMeter | Performance | XML/GUI | Thread groups, listeners |
| Gatling | Performance | Java/Scala | Code-first, reactive |
| flutter_test | Mobile | Dart | Widget testing |
| integration_test | Mobile | Dart | Device/emulator testing |
| patrol | Mobile | Dart | Native system interactions |
