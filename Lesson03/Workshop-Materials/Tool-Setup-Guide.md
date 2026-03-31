# Tool Setup Guide - Test Automation Frameworks

This guide provides comprehensive setup instructions for all tools, libraries, and environments needed for the Test Automation Frameworks lesson.

---

## 🎯 Prerequisites Overview

### **System Requirements**
- **Operating System:** Windows 10+, macOS 10.15+, or Ubuntu 18.04+
- **RAM:** Minimum 8GB, Recommended 16GB
- **Storage:** Minimum 10GB free space
- **Internet:** Stable connection for downloading dependencies
- **Permissions:** Administrator/sudo access for software installation

### **Time Estimation**
- **Basic setup:** 45-60 minutes
- **Full setup with all tools:** 90-120 minutes
- **Verification and testing:** 30 minutes

---

## ☕ Java Development Environment

### **Java JDK Installation**

#### **Option 1: Oracle JDK (Recommended for Enterprise)**
1. **Download:**
   - Visit: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
   - Select JDK 17 or 21 LTS version for your OS
   - Accept license agreement

2. **Windows Installation:**
   ```bash
   # Run the downloaded installer (.exe)
   # Follow installation wizard
   # Add to system PATH if not automatically added
   ```

3. **macOS Installation:**
   ```bash
   # Install downloaded .dmg file
   # Or use Homebrew:
   brew install openjdk@17
   ```

4. **Linux Installation:**
   ```bash
   # Ubuntu/Debian
   sudo apt update
   sudo apt install openjdk-17-jdk
   
   # CentOS/RHEL
   sudo yum install java-17-openjdk-devel
   ```

#### **Option 2: SDKMAN (Multi-platform Java Manager)**
```bash
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Install Java
sdk install java 17.0.8-tem
sdk install java 21.0.1-tem

# Switch between versions
sdk use java 17.0.8-tem
```

### **Environment Variables Setup**

#### **Windows (Command Prompt)**
```cmd
setx JAVA_HOME "C:\Program Files\Java\jdk-17"
setx PATH "%PATH%;%JAVA_HOME%\bin"
```

#### **Windows (PowerShell)**
```powershell
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17", [EnvironmentVariableTarget]::Machine)
[Environment]::SetEnvironmentVariable("PATH", $env:PATH + ";$env:JAVA_HOME\bin", [EnvironmentVariableTarget]::Machine)
```

#### **macOS/Linux (Bash/Zsh)**
```bash
# Add to ~/.bashrc or ~/.zshrc
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$PATH:$JAVA_HOME/bin
```

### **Verification**
```bash
java -version
javac -version
echo $JAVA_HOME
```

---

## 🛠️ Integrated Development Environment (IDE)

### **IntelliJ IDEA (Recommended)**

#### **Community Edition (Free)**
1. **Download:** https://www.jetbrains.com/idea/download/
2. **Install:** Follow platform-specific installer
3. **Initial Setup:**
   ```
   - Configure JDK (File > Project Structure > Project Settings > Project)
   - Install plugins:
     * TestNG
     * Cucumber for Java
     * Allure
     * Selenium Support
   ```

#### **Essential IntelliJ Plugins**
```
File > Settings > Plugins > Browse Repositories
- TestNG-J
- Cucumber for Java
- Gherkin
- Allure
- Selenium Page Object Generator
- Maven Helper
- SonarLint
```

### **Eclipse IDE (Alternative)**
1. **Download:** https://www.eclipse.org/downloads/
2. **Install Eclipse IDE for Java Developers**
3. **Install TestNG Plugin:**
   ```
   Help > Eclipse Marketplace > Search "TestNG"
   ```

### **VS Code (Lightweight Option)**
1. **Download:** https://code.visualstudio.com/
2. **Install Java Extensions:**
   ```
   - Extension Pack for Java
   - Test Runner for Java
   - Debugger for Java
   - Maven for Java
   - Gradle for Java
   ```

---

## 📦 Build Tools

### **Apache Maven**

#### **Installation**
1. **Download:** https://maven.apache.org/download.cgi
2. **Extract to desired location**

#### **Windows Setup**
```cmd
# Extract to C:\apache-maven-3.9.5
setx M2_HOME "C:\apache-maven-3.9.5"
setx PATH "%PATH%;%M2_HOME%\bin"
```

#### **macOS/Linux Setup**
```bash
# Using package manager
# Ubuntu
sudo apt install maven

# macOS
brew install maven

# Manual installation
export M2_HOME=/opt/apache-maven-3.9.5
export PATH=$PATH:$M2_HOME/bin
```

#### **Verification**
```bash
mvn -version
```

#### **Maven Configuration (Optional)**
Create `~/.m2/settings.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 
          http://maven.apache.org/xsd/settings-1.0.0.xsd">
  
  <localRepository>${user.home}/.m2/repository</localRepository>
  
  <mirrors>
    <mirror>
      <id>central-mirror</id>
      <name>Maven Central Mirror</name>
      <url>https://repo1.maven.org/maven2</url>
      <mirrorOf>central</mirrorOf>
    </mirror>
  </mirrors>
</settings>
```

### **Gradle (Alternative)**

#### **Installation Using SDKMAN**
```bash
sdk install gradle 8.4
```

#### **Manual Installation**
1. **Download:** https://gradle.org/releases/
2. **Extract and set PATH**

#### **Windows Setup**
```cmd
setx GRADLE_HOME "C:\gradle-8.4"
setx PATH "%PATH%;%GRADLE_HOME%\bin"
```

#### **Verification**
```bash
gradle -version
```

---

## 🌐 Browser and Driver Setup

### **Browser Installation**

#### **Google Chrome**
```bash
# Windows: Download from https://www.google.com/chrome/
# macOS
brew install --cask google-chrome
# Ubuntu
wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" | sudo tee /etc/apt/sources.list.d/google-chrome.list
sudo apt update
sudo apt install google-chrome-stable
```

#### **Mozilla Firefox**
```bash
# Windows: Download from https://www.mozilla.org/firefox/
# macOS
brew install --cask firefox
# Ubuntu
sudo apt install firefox
```

#### **Microsoft Edge**
```bash
# Windows: Usually pre-installed
# macOS
brew install --cask microsoft-edge
# Ubuntu
curl https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > microsoft.gpg
sudo install -o root -g root -m 644 microsoft.gpg /etc/apt/trusted.gpg.d/
echo "deb [arch=amd64,arm64,armhf signed-by=/etc/apt/trusted.gpg.d/microsoft.gpg] https://packages.microsoft.com/repos/edge stable main" | sudo tee /etc/apt/sources.list.d/microsoft-edge-dev.list
sudo apt update
sudo apt install microsoft-edge-stable
```

### **WebDriver Management**

#### **Option 1: WebDriverManager (Recommended)**
Add to your Maven `pom.xml`:
```xml
<dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.6.2</version>
</dependency>
```

Usage in code:
```java
import io.github.bonigarcia.wdm.WebDriverManager;

// Automatic driver management
WebDriverManager.chromedriver().setup();
WebDriver driver = new ChromeDriver();
```

#### **Option 2: Manual Driver Download**
1. **ChromeDriver:** https://chromedriver.chromium.org/
2. **GeckoDriver (Firefox):** https://github.com/mozilla/geckodriver/releases
3. **EdgeDriver:** https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/

**Add drivers to PATH or specify location:**
```java
System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
WebDriver driver = new ChromeDriver();
```

---

## 🐳 Docker Setup (Optional but Recommended)

### **Docker Desktop Installation**

#### **Windows**
1. **Download:** https://www.docker.com/products/docker-desktop/
2. **Install Docker Desktop**
3. **Enable WSL 2 integration (if using WSL)**

#### **macOS**
```bash
# Using Homebrew
brew install --cask docker

# Or download from https://www.docker.com/products/docker-desktop/
```

#### **Linux**
```bash
# Ubuntu
sudo apt update
sudo apt install apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt update
sudo apt install docker-ce

# Start Docker service
sudo systemctl start docker
sudo systemctl enable docker

# Add user to docker group
sudo usermod -aG docker $USER
```

### **Selenium Grid with Docker**
```bash
# Start Selenium Grid
docker run -d -p 4444:4444 -p 7900:7900 --shm-size=2g selenium/standalone-chrome:4.15.0

# Verify Grid is running
curl http://localhost:4444/status
```

### **Docker Compose Setup**
Create `docker-compose.yml`:
```yaml
version: '3.8'
services:
  selenium-hub:
    image: selenium/hub:4.15.0
    container_name: selenium-hub
    ports:
      - "4444:4444"

  chrome:
    image: selenium/node-chrome:4.15.0
    shm_size: 2gb
    depends_on:
      - selenium-hub
    environment:
      - HUB_HOST=selenium-hub
      - HUB_PORT=4444

  firefox:
    image: selenium/node-firefox:4.15.0
    shm_size: 2gb
    depends_on:
      - selenium-hub
    environment:
      - HUB_HOST=selenium-hub
      - HUB_PORT=4444
```

Start with:
```bash
docker-compose up -d
```

---

## 📁 Project Setup

### **Maven Project Structure**
```bash
mkdir automation-framework
cd automation-framework

# Create Maven project
mvn archetype:generate \
    -DgroupId=com.example.automation \
    -DartifactId=test-framework \
    -DarchetypeArtifactId=maven-archetype-quickstart \
    -DinteractiveMode=false

cd test-framework
```

### **Complete pom.xml Template**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example.automation</groupId>
    <artifactId>test-framework</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        
        <!-- Dependency Versions -->
        <selenium.version>4.15.0</selenium.version>
        <testng.version>7.8.0</testng.version>
        <rest-assured.version>5.3.2</rest-assured.version>
        <allure.version>2.24.0</allure.version>
        <webdrivermanager.version>5.6.2</webdrivermanager.version>
        <jackson.version>2.15.2</jackson.version>
        <logback.version>1.4.11</logback.version>
    </properties>

    <dependencies>
        <!-- Selenium WebDriver -->
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>${selenium.version}</version>
        </dependency>

        <!-- WebDriver Manager -->
        <dependency>
            <groupId>io.github.bonigarcia</groupId>
            <artifactId>webdrivermanager</artifactId>
            <version>${webdrivermanager.version}</version>
        </dependency>

        <!-- TestNG -->
        <dependency>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
            <version>${testng.version}</version>
        </dependency>

        <!-- REST-assured -->
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>rest-assured</artifactId>
            <version>${rest-assured.version}</version>
        </dependency>

        <!-- JSON Schema Validation -->
        <dependency>
            <groupId>io.rest-assured</groupId>
            <artifactId>json-schema-validator</artifactId>
            <version>${rest-assured.version}</version>
        </dependency>

        <!-- Allure TestNG -->
        <dependency>
            <groupId>io.qameta.allure</groupId>
            <artifactId>allure-testng</artifactId>
            <version>${allure.version}</version>
        </dependency>

        <!-- Jackson for JSON handling -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${jackson.version}</version>
        </dependency>

        <!-- Logging -->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>${logback.version}</version>
        </dependency>

        <!-- Apache Commons -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.13.0</version>
        </dependency>

        <!-- AssertJ for fluent assertions -->
        <dependency>
            <groupId>org.assertj</groupId>
            <artifactId>assertj-core</artifactId>
            <version>3.24.2</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Compiler Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>

            <!-- Surefire Plugin for Test Execution -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.2</version>
                <configuration>
                    <suiteXmlFiles>
                        <suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>
                    </suiteXmlFiles>
                    <systemPropertyVariables>
                        <browser>${browser}</browser>
                        <environment>${environment}</environment>
                    </systemPropertyVariables>
                </configuration>
            </plugin>

            <!-- Allure Maven Plugin -->
            <plugin>
                <groupId>io.qameta.allure</groupId>
                <artifactId>allure-maven</artifactId>
                <version>2.12.0</version>
                <configuration>
                    <reportVersion>${allure.version}</reportVersion>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <profiles>
        <!-- UI Tests Profile -->
        <profile>
            <id>ui-tests</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-surefire-plugin</artifactId>
                        <configuration>
                            <includes>
                                <include>**/*UITest.java</include>
                            </includes>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>

        <!-- API Tests Profile -->
        <profile>
            <id>api-tests</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-surefire-plugin</artifactId>
                        <configuration>
                            <includes>
                                <include>**/*APITest.java</include>
                            </includes>
                        </configuration>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
</project>
```

### **TestNG Configuration**
Create `src/test/resources/testng.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="Automation Test Suite" verbose="1" parallel="methods" thread-count="3">
    
    <test name="UI Tests">
        <classes>
            <class name="com.example.automation.tests.LoginTest"/>
            <class name="com.example.automation.tests.ProductSearchTest"/>
        </classes>
    </test>
    
    <test name="API Tests">
        <classes>
            <class name="com.example.automation.api.UserAPITest"/>
            <class name="com.example.automation.api.ProductAPITest"/>
        </classes>
    </test>
    
</suite>
```

### **Logging Configuration**
Create `src/test/resources/logback.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>target/test-execution.log</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <logger name="com.example.automation" level="INFO"/>
    <logger name="io.rest-assured" level="INFO"/>
    <logger name="org.apache.http" level="WARN"/>

    <root level="INFO">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="FILE"/>
    </root>
    
</configuration>
```

---

## 🔧 Additional Tools

### **Git Setup**
```bash
# Install Git
# Windows: https://git-scm.com/download/win
# macOS: brew install git
# Ubuntu: sudo apt install git

# Configure Git
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Initialize repository
git init
git add .
git commit -m "Initial commit"
```

### **Allure Reporting**
```bash
# Install Allure (requires Node.js)
npm install -g allure-commandline

# Or using package managers
# macOS
brew install allure

# Windows (using Scoop)
scoop install allure
```

### **Database Tools (Optional)**
```bash
# PostgreSQL
# Windows: Download from https://www.postgresql.org/download/
# macOS: brew install postgresql
# Ubuntu: sudo apt install postgresql postgresql-contrib

# MySQL
# Windows: Download from https://dev.mysql.com/downloads/mysql/
# macOS: brew install mysql
# Ubuntu: sudo apt install mysql-server
```

---

## ✅ Verification Script

### **Complete Setup Verification**
Create `verify-setup.sh` (Linux/macOS) or `verify-setup.bat` (Windows):

```bash
#!/bin/bash
echo "Testing Automation Framework Setup"
echo "=================================="

# Java
echo "Checking Java..."
java -version
if [ $? -eq 0 ]; then
    echo "✓ Java is installed"
else
    echo "✗ Java is not installed or not in PATH"
fi

# Maven
echo "Checking Maven..."
mvn -version
if [ $? -eq 0 ]; then
    echo "✓ Maven is installed"
else
    echo "✗ Maven is not installed or not in PATH"
fi

# Git
echo "Checking Git..."
git --version
if [ $? -eq 0 ]; then
    echo "✓ Git is installed"
else
    echo "✗ Git is not installed or not in PATH"
fi

# Chrome
echo "Checking Chrome..."
google-chrome --version 2>/dev/null || chrome.exe --version 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✓ Chrome is installed"
else
    echo "✗ Chrome is not installed or not in PATH"
fi

# Firefox
echo "Checking Firefox..."
firefox --version 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✓ Firefox is installed"
else
    echo "✗ Firefox is not installed or not in PATH"
fi

# Docker (optional)
echo "Checking Docker..."
docker --version 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✓ Docker is installed"
else
    echo "! Docker is not installed (optional)"
fi

echo "=================================="
echo "Setup verification complete!"
```

### **Java Test Program**
Create `TestSetup.java`:
```java
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestSetup {
    public static void main(String[] args) {
        System.out.println("Testing Automation Setup...");
        
        try {
            // Setup ChromeDriver
            WebDriverManager.chromedriver().setup();
            
            // Create WebDriver instance
            WebDriver driver = new ChromeDriver();
            
            // Navigate to a test page
            driver.get("https://www.google.com");
            
            // Get page title
            String title = driver.getTitle();
            System.out.println("Page title: " + title);
            
            // Close browser
            driver.quit();
            
            System.out.println("✓ Selenium setup working correctly!");
            
        } catch (Exception e) {
            System.err.println("✗ Setup test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

Compile and run:
```bash
javac -cp "selenium-java-4.15.0.jar:webdrivermanager-5.6.2.jar:." TestSetup.java
java -cp "selenium-java-4.15.0.jar:webdrivermanager-5.6.2.jar:." TestSetup
```

---

## 🚨 Common Issues and Troubleshooting

### **Java Issues**
**Problem:** `JAVA_HOME not found`
**Solution:**
```bash
# Find Java installation
which java
# or
/usr/libexec/java_home -V  # macOS only

# Set JAVA_HOME correctly
export JAVA_HOME=/path/to/java
```

**Problem:** `UnsupportedClassVersionError`
**Solution:** Ensure Java version compatibility between JDK and compiled code

### **WebDriver Issues**
**Problem:** `WebDriverException: Driver executable not found`
**Solution:**
1. Use WebDriverManager for automatic driver management
2. Manually download and add driver to PATH
3. Specify driver location explicitly

**Problem:** `Chrome browser crashes`
**Solution:**
```java
ChromeOptions options = new ChromeOptions();
options.addArguments("--no-sandbox");
options.addArguments("--disable-dev-shm-usage");
options.addArguments("--disable-gpu");
WebDriver driver = new ChromeDriver(options);
```

### **Maven Issues**
**Problem:** `Dependencies not downloading`
**Solution:**
```bash
# Clean and reload dependencies
mvn clean install -U

# Clear local repository
rm -rf ~/.m2/repository
mvn clean install
```

**Problem:** `Plugin execution not covered by lifecycle`
**Solution:** Add `<pluginManagement>` section in pom.xml or use more recent Maven version

### **Docker Issues**
**Problem:** `Docker daemon not running`
**Solution:**
```bash
# Start Docker service
sudo systemctl start docker  # Linux
# Or start Docker Desktop application
```

**Problem:** `Permission denied accessing Docker`
**Solution:**
```bash
# Add user to docker group
sudo usermod -aG docker $USER
# Log out and log back in
```

### **Network Issues**
**Problem:** Corporate firewall blocking downloads
**Solution:**
1. Configure Maven proxy settings
2. Use local artifact repository
3. Download dependencies manually

---

## 📚 Next Steps

### **After Setup Completion**
1. **Run verification scripts** to ensure everything works
2. **Create a simple test** to validate the setup
3. **Review project structure** and familiarize with folders
4. **Configure IDE** with proper plugins and settings
5. **Set up version control** for your automation project

### **Learning Resources**
- **Selenium Documentation:** https://selenium.dev/documentation/
- **TestNG Documentation:** https://testng.org/doc/
- **REST-assured Documentation:** https://rest-assured.io/
- **Allure Documentation:** https://docs.qameta.io/allure/

### **Community Support**
- **Selenium Community:** https://selenium.dev/support/
- **Stack Overflow:** Use tags `selenium`, `testng`, `java`
- **GitHub Discussions:** Framework-specific repositories

---

This comprehensive setup guide should prepare your environment for all the hands-on activities and labs in the Test Automation Frameworks lesson. If you encounter any issues not covered here, document them during the lesson for continuous improvement of this guide.