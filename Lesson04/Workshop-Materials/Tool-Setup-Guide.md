# Tool Setup Guide — Test Automation Tools

Complete installation and configuration instructions for all tools covered in Lesson04.

---

## ☕ Java and Maven (Required for Selenium, REST Assured, Gatling)

### Install Java 17

**Windows (Winget):**
```powershell
winget install Microsoft.OpenJDK.17
```

**macOS (Homebrew):**
```bash
brew install openjdk@17
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

**Linux (apt):**
```bash
sudo apt update && sudo apt install openjdk-17-jdk -y
```

Verify:
```bash
java -version   # Should show: openjdk version "17.x.x"
```

### Install Maven 3.8+

**Windows (Winget):**
```powershell
winget install Apache.Maven
```

**macOS:**
```bash
brew install maven
```

**Linux:**
```bash
sudo apt install maven -y
```

Verify:
```bash
mvn -version   # Should show: Apache Maven 3.x.x
```

---

## 🌐 Node.js 18+ (Required for Cypress and Puppeteer)

**Windows:**
```powershell
winget install OpenJS.NodeJS.LTS
```

**macOS:**
```bash
brew install node
```

**Linux:**
```bash
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install nodejs -y
```

Verify:
```bash
node -version   # v18.x.x or higher
npm -version    # 9.x.x or higher
```

---

## 🖥️ Web UI Tools

### Selenium WebDriver

No browser driver installation needed with Selenium 4.6+ — Selenium Manager handles it automatically.

**Maven project setup:**
```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=selenium-tests \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

Add to `pom.xml`:
```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>

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

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.2.5</version>
        </plugin>
    </plugins>
</build>
```

Run tests:
```bash
mvn test
```

---

### Cypress

```bash
# Create project
mkdir cypress-project && cd cypress-project
npm init -y
npm install --save-dev cypress

# Open Cypress (first run creates folder structure)
npx cypress open

# Run headlessly
npx cypress run

# Run specific spec headlessly
npx cypress run --spec "cypress/e2e/login.cy.js" --browser chrome
```

**cypress.config.js:**
```javascript
const { defineConfig } = require('cypress');

module.exports = defineConfig({
  e2e: {
    baseUrl: 'http://localhost:3000',
    viewportWidth: 1280,
    viewportHeight: 720,
    video: true,
    screenshotOnRunFailure: true,
    defaultCommandTimeout: 10000,
  },
});
```

---

### Puppeteer

```bash
# Install Puppeteer (downloads Chromium automatically)
npm install puppeteer

# Install lightweight version (bring your own Chrome)
npm install puppeteer-core
```

**Basic headless script:**
```javascript
// test-headless.js
const puppeteer = require('puppeteer');
(async () => {
  const browser = await puppeteer.launch({
    headless: 'new',
    args: ['--no-sandbox', '--disable-setuid-sandbox']  // Required in CI
  });
  const page = await browser.newPage();
  await page.goto('https://example.com');
  console.log(await page.title());
  await browser.close();
})();
```

```bash
node test-headless.js
```

---

## 🔌 API Testing Tools

### Postman Desktop

1. Download from https://www.postman.com/downloads/
2. Install and sign in (free account)
3. Create a workspace for the course

### Newman CLI

```bash
npm install -g newman newman-reporter-htmlextra

# Verify
newman -version   # 6.x.x

# Run a collection
newman run collection.json -e environment.json \
  --reporters cli,htmlextra \
  --reporter-htmlextra-export report.html
```

### REST Assured (Maven)

Add to `pom.xml`:
```xml
<dependencies>
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>5.4.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>json-path</artifactId>
        <version>5.4.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.hamcrest</groupId>
        <artifactId>hamcrest</artifactId>
        <version>2.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## ⚡ Performance Testing Tools

### Apache JMeter 5.6+

1. Download: https://jmeter.apache.org/download_jmeter.cgi
2. Extract to a directory (e.g., `C:\jmeter` on Windows)
3. Add `bin/` to your system PATH

```bash
# Verify
jmeter -v

# Launch GUI
jmeter

# Run headlessly
jmeter -n -t test-plan.jmx -l results.jtl -e -o jmeter-report/
```

**Recommended JMeter plugins:**
- JMeter Plugins Manager: https://jmeter-plugins.org/
- Custom Thread Groups plugin
- PerfMon (server monitoring)

**JVM memory tuning (for large tests):**
```bash
# Edit bin/jmeter (Linux/macOS) or bin/jmeter.bat (Windows)
JVM_ARGS="-Xms1g -Xmx4g"
```

---

### Gatling (Java DSL, Maven)

Add to `pom.xml`:
```xml
<dependencies>
    <dependency>
        <groupId>io.gatling</groupId>
        <artifactId>gatling-app</artifactId>
        <version>3.10.5</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>io.gatling</groupId>
            <artifactId>gatling-maven-plugin</artifactId>
            <version>4.9.6</version>
            <configuration>
                <simulationClass>performance.MySimulation</simulationClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Run simulation:
```bash
mvn gatling:test

# HTML report generated at:
# target/gatling/<simulation-name>-<timestamp>/index.html
```

---

## 📱 Flutter SDK

### Install Flutter

**Windows:**
1. Download Flutter SDK: https://docs.flutter.dev/get-started/install/windows
2. Extract to `C:\flutter`
3. Add `C:\flutter\bin` to your system PATH

**macOS:**
```bash
brew install --cask flutter
```

**Linux:**
```bash
sudo snap install flutter --classic
```

### Verify installation
```bash
flutter doctor
# Fix all issues (especially Android SDK and licenses)
flutter doctor --android-licenses
```

### Android emulator setup
1. Install **Android Studio**: https://developer.android.com/studio
2. Open AVD Manager → Create Virtual Device
3. Choose Pixel 6 → API 34 (Android 14)
4. Start the emulator

### Run tests
```bash
# Unit and widget tests (no device needed)
flutter test

# Integration tests (requires emulator or device)
flutter test integration_test/ -d emulator-5554

# List connected devices
flutter devices
```

### pubspec.yaml for testing
```yaml
dev_dependencies:
  flutter_test:
    sdk: flutter
  integration_test:
    sdk: flutter
  patrol: ^3.6.0
  fake_async: ^1.3.1
  mocktail: ^1.0.1
```

After adding dependencies:
```bash
flutter pub get
```

---

## 🔧 IDE Setup

### IntelliJ IDEA (Java — Selenium, REST Assured, Gatling)
- Install **Selenium** plugin (optional, for element inspection)
- Enable Maven auto-import
- Install **SonarLint** for code quality

### VS Code (JavaScript — Cypress, Puppeteer)
- Install **Cypress Snippets** extension
- Install **ESLint** extension
- Install **REST Client** extension for manual API testing

### Android Studio / VS Code (Flutter)
- Install **Flutter** extension (VS Code) or plugin (Android Studio)
- Install **Dart** extension
- Enable Flutter DevTools for debugging

---

## ✅ Pre-Session Verification Script

Run this script before the session to verify all tools are ready:

```bash
#!/bin/bash
echo "=== Lesson04 Environment Check ==="

check() {
  if command -v "$1" &>/dev/null; then
    echo "✓ $1: $(command "$1" $2 2>&1 | head -1)"
  else
    echo "✗ $1: NOT FOUND"
  fi
}

check java "-version"
check mvn "--version"
check node "--version"
check npm "--version"
check newman "--version"
check jmeter "-v"
check flutter "--version"

echo ""
echo "Node packages:"
npx cypress -v 2>/dev/null && echo "✓ cypress" || echo "✗ cypress: not installed"
node -e "require('puppeteer')" 2>/dev/null && echo "✓ puppeteer" || echo "✗ puppeteer: not installed"
```

**Windows equivalent (PowerShell):**
```powershell
@("java", "mvn", "node", "npm", "newman", "jmeter", "flutter") | ForEach-Object {
    $v = & $_ --version 2>&1 | Select-Object -First 1
    if ($LASTEXITCODE -eq 0) { Write-Host "✓ $_: $v" }
    else { Write-Host "✗ $_: NOT FOUND" -ForegroundColor Red }
}
```
