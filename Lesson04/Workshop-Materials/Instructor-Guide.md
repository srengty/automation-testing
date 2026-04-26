# Instructor Guide — Test Automation Tools

## 📋 Session Overview

**Session:** Lesson04 — Test Automation Tools  
**Duration:** 6 hours (360 minutes)  
**Format:** Lecture + live demos + 3 workshops  
**Class size:** 10–30 students  

---

## 🎯 Learning Objectives

By the end of this session, students will be able to:
1. Explain the purpose and architecture of Selenium, Cypress, and Puppeteer
2. Write basic automated tests using all three Web UI tools
3. Create Postman collections with test scripts and run them via Newman
4. Write REST Assured tests using the Given-When-Then pattern
5. Build a JMeter test plan and execute it headlessly
6. Write a Gatling simulation using the Java DSL
7. Write Flutter widget and integration tests
8. Integrate multiple tools into a CI/CD pipeline

---

## ⏰ Detailed Session Plan

### Part 1: Foundations (35 minutes)

| Time | Activity | Notes |
|------|----------|-------|
| 0:00–0:05 | Welcome, attendance, environment check | Ask students to open terminals and verify installs |
| 0:05–0:10 | Agenda walkthrough (Slide 2) | Poll: who has used each tool before? |
| 0:10–0:25 | Tool landscape overview (Slides 3–4) | Use the tool landscape table as a discussion anchor |
| 0:25–0:35 | Tool selection criteria (Slide 5) | Ask: "what factors matter most to your team?" |

**Key teaching points:**
- Emphasise that no single tool does everything — each has a specific sweet spot
- The test pyramid guides tool selection (unit → API → UI)

---

### Part 2: Web UI Testing (110 minutes)

| Time | Activity | Notes |
|------|----------|-------|
| 0:35–1:15 | Selenium slides 6–8 | Live demo: run a simple Selenium test against the-internet.herokuapp.com |
| 1:15–2:15 | Cypress slides 9–10 | Live demo: `npx cypress open`, run login test |
| 1:45–2:15 | Puppeteer slides 11–12 | Live demo: run headless script, show screenshot output |
| 2:15–2:25 | Comparison slide 13 | Group discussion: when would you pick each? |
| 2:25–2:55 | **Workshop 1** | Circulate; help with driver setup issues |

**Common issues:**
- Selenium: Chrome version mismatch → remind students Selenium 4.6+ auto-manages drivers
- Cypress: Port conflicts → `npx cypress open --port 3001`
- Puppeteer: `--no-sandbox` flag needed in some CI environments

**Transition:** "Now that we can test the UI, let's go one level deeper — the API."

---

### Part 3: API Testing (100 minutes)

| Time | Activity | Notes |
|------|----------|-------|
| 2:55–3:25 | Postman slides 14–16 | Live demo: create collection for reqres.in, show environment variables |
| 3:25–3:55 | REST Assured slides 17–18 | Live demo: run Maven test from IntelliJ, show failing assertion output |
| 3:55–4:05 | Comparison slide 19 | Key point: REST Assured lives in the same codebase as production code |
| 4:05–4:35 | **Workshop 2** | Pair students: one on Postman, one on REST Assured — then swap |

**Key teaching points:**
- API tests are faster, less brittle, and cheaper than UI tests — test business logic here
- Postman is excellent for exploration; REST Assured is better for automated regression
- Newman enables running Postman collections in GitHub Actions / Jenkins

---

### Part 4: Performance Testing (90 minutes)

| Time | Activity | Notes |
|------|----------|-------|
| 4:35–5:05 | JMeter slides 20–22 | Live demo: build thread group in GUI, then run via CLI |
| 5:05–5:35 | Gatling slides 23–24 | Live demo: `mvn gatling:test`, show HTML report |
| 5:35–5:45 | Comparison slide 25 | Highlight: Gatling is preferred for teams that value code review |

**Key teaching points:**
- Performance testing is often neglected — even basic load tests catch issues pre-production
- Start with realistic user counts (not 10,000 users on day 1)
- JMeter GUI is for authoring; always use CLI for actual test runs

---

### Part 5: Flutter Mobile Testing (55 minutes)

| Time | Activity | Notes |
|------|----------|-------|
| 5:45–6:10 | Flutter overview + widget tests (Slides 26–27) | Live demo: `flutter test` in counter_app |
| 6:10–6:40 | Integration tests + Patrol (Slides 28–29) | Show emulator test run if time allows |
| 6:40–7:20 | **Workshop 3** | JMeter exercise (20 min) + Flutter exercise (20 min) |

**Key teaching points:**
- Widget tests do NOT need a physical device — they run in a test environment in milliseconds
- Integration tests need an emulator or device — they test the real app experience
- Patrol solves the biggest gap in Flutter testing: native OS dialogs

---

### Wrap-up (40 minutes)

| Time | Activity | Notes |
|------|----------|-------|
| 7:20–7:35 | CI/CD integration slide 31 | Walk through the GitHub Actions YAML step by step |
| 7:35–7:45 | Best practices slide 32 | Ask: "which of these does your current project follow?" |
| 7:45–7:55 | Assessment discussion slide 33 | Use questions as group discussion, not a quiz |
| 7:55–8:00 | Summary slide 34 + feedback | Ask students to submit session feedback |

---

## 🔧 Setup Verification Checklist

Run this check at the start of the session (0:00–0:05):

```bash
# Java and Maven
java -version        # expect: 17+
mvn -version         # expect: 3.8+

# Node.js and npm
node -version        # expect: 18+
npm -version         # expect: 9+

# Postman/Newman
newman -version      # expect: 6+

# JMeter
jmeter -v            # expect: 5.6+

# Flutter
flutter doctor       # No critical issues
```

---

## 🆘 Troubleshooting Guide

| Issue | Solution |
|-------|----------|
| Selenium: browser opens and closes immediately | Missing `driver.quit()` at wrong place; use `@AfterEach` |
| Cypress: "Cannot find module" | Run `npm install` in project root |
| Puppeteer: crashes in CI | Add `args: ['--no-sandbox', '--disable-setuid-sandbox']` |
| Newman: collection not found | Check file path is relative to current directory |
| REST Assured: SSL error | Add `relaxedHTTPSValidation()` to `given()` |
| JMeter: OutOfMemoryError | Increase heap: `JVM_ARGS="-Xmx2g" jmeter` |
| Gatling: compilation error | Check Java DSL version matches `gatling-maven-plugin` version |
| Flutter: `flutter doctor` shows issues | Check Android SDK path; run `flutter doctor --android-licenses` |

---

## 📊 Assessment Strategy

### Formative (during session)
- Poll questions at the start of each part (hands raised)
- Quick "traffic light" check: green = got it, yellow = need more, red = confused
- Code review during workshops — circulate and give feedback

### Summative (end of session)
- Discussion questions from Slide 33 — students answer in pairs, then share
- Optional take-home: implement a complete test suite for a given API using any two tools covered

---

## 📚 Additional Resources

- Selenium: https://www.selenium.dev/documentation/
- Cypress: https://docs.cypress.io
- Puppeteer: https://pptr.dev
- Postman Learning Center: https://learning.postman.com
- REST Assured: https://rest-assured.io
- JMeter User Manual: https://jmeter.apache.org/usermanual/
- Gatling Docs: https://gatling.io/docs/
- Flutter Testing: https://docs.flutter.dev/testing
- Patrol: https://patrol.leancode.co
