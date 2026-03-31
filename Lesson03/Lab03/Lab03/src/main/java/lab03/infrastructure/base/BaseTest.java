package lab03.infrastructure.base;

import lab03.infrastructure.config.ConfigManager;
import lab03.infrastructure.driver.DriverManager;
import lab03.infrastructure.utils.ScreenshotUtils;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * Base Test Class for Test Automation Framework
 * 
 * This abstract class provides common test infrastructure and lifecycle management
 * for all test classes in the automation framework.
 * 
 * Features:
 * - WebDriver lifecycle management
 * - Test result logging
 * - Screenshot on failure
 * - Configuration access
 * - Test reporting integration
 * - Data provider support
 */
public abstract class BaseTest {
    
    protected DriverManager driverManager;
    protected ConfigManager config;
    
    /**
     * Suite-level setup - runs once before all tests in the suite
     */
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.out.println("=== Starting Test Suite ===");
        config = ConfigManager.getInstance();
        
        // Print configuration for debugging
        if (config.getBoolean("debug.config", false)) {
            config.printConfiguration();
        }
        
        setupTestEnvironment();
    }
    
    /**
     * Test-level setup - runs before each test method
     */
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        String testName = method.getName();
        String className = method.getDeclaringClass().getSimpleName();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Starting Test: " + className + "." + testName);
        System.out.println("Environment: " + config.getCurrentEnvironment());
        System.out.println("Browser: " + config.getBrowser());
        System.out.println("=".repeat(60));
        
        // Initialize WebDriver for this test
        driverManager = new DriverManager();
        driverManager.initializeDriver();
        
        // Perform test-specific setup
        setupTest(method);
    }
    
    /**
     * Test-level cleanup - runs after each test method
     */
    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result, Method method) {
        String testName = method.getName();
        String className = method.getDeclaringClass().getSimpleName();
        String status = getTestStatus(result);
        
        System.out.println("\n" + "-".repeat(60));
        System.out.println("Finished Test: " + className + "." + testName);
        System.out.println("Status: " + status);
        System.out.println("Duration: " + (result.getEndMillis() - result.getStartMillis()) + "ms");
        
        // Handle test failure
        if (result.getStatus() == ITestResult.FAILURE) {
            handleTestFailure(result, testName);
        }
        
        // Perform test-specific cleanup
        cleanupTest(result, method);
        
        // Quit WebDriver
        if (driverManager != null) {
            driverManager.quitDriver();
        }
        
        System.out.println("-".repeat(60));
    }
    
    /**
     * Suite-level cleanup - runs once after all tests in the suite
     */
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        System.out.println("\n=== Test Suite Completed ===");
        cleanupTestEnvironment();
    }
    
    /**
     * Handle test failure - take screenshot and log error details
     */
    private void handleTestFailure(ITestResult result, String testName) {
        System.err.println("TEST FAILED: " + testName);
        System.err.println("Error: " + result.getThrowable().getMessage());
        
        // Take screenshot if WebDriver is available
        if (DriverManager.isDriverInitialized()) {
            try {
                String screenshotPath = ScreenshotUtils.captureScreenshot(testName);
                System.out.println("Screenshot saved: " + screenshotPath);
                
                // Attach to Allure report if available
                attachScreenshotToReport(screenshotPath);
            } catch (Exception e) {
                System.err.println("Failed to capture screenshot: " + e.getMessage());
            }
        }
        
        // Log additional debugging information
        logFailureDetails(result);
    }
    
    /**
     * Attach screenshot to Allure report
     */
    private void attachScreenshotToReport(String screenshotPath) {
        try {
            // Allure screenshot attachment
            io.qameta.allure.Allure.addAttachment("Screenshot", 
                new java.io.FileInputStream(screenshotPath));
        } catch (Exception e) {
            System.err.println("Failed to attach screenshot to report: " + e.getMessage());
        }
    }
    
    /**
     * Log detailed failure information
     */
    private void logFailureDetails(ITestResult result) {
        if (DriverManager.isDriverInitialized()) {
            try {
                System.err.println("Current URL: " + driverManager.getCurrentUrl());
                System.err.println("Page Title: " + driverManager.getCurrentTitle());
                
                // Log browser console errors
                logBrowserConsoleErrors();
                
            } catch (Exception e) {
                System.err.println("Error while collecting failure details: " + e.getMessage());
            }
        }
    }
    
    /**
     * Log browser console errors
     */
    private void logBrowserConsoleErrors() {
        try {
            Object logs = driverManager.executeJavaScript(
                "return window.console ? window.console.errors || [] : []");
            if (logs != null) {
                System.err.println("Browser Console Errors: " + logs);
            }
        } catch (Exception e) {
            // Ignore if console logs are not available
        }
    }
    
    /**
     * Get test status as string
     */
    private String getTestStatus(ITestResult result) {
        return switch (result.getStatus()) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP -> "SKIPPED";
            default -> "UNKNOWN";
        };
    }
    
    // Abstract methods for subclasses to implement
    
    /**
     * Setup test environment - called once per suite
     * Override in subclasses for environment-specific setup
     */
    protected void setupTestEnvironment() {
        // Default implementation - can be overridden
        System.out.println("Setting up test environment...");
    }
    
    /**
     * Setup individual test - called before each test method
     * Override in subclasses for test-specific setup
     */
    protected void setupTest(Method method) {
        // Default implementation - can be overridden
    }
    
    /**
     * Cleanup individual test - called after each test method
     * Override in subclasses for test-specific cleanup
     */
    protected void cleanupTest(ITestResult result, Method method) {
        // Default implementation - can be overridden
    }
    
    /**
     * Cleanup test environment - called once per suite
     * Override in subclasses for environment-specific cleanup
     */
    protected void cleanupTestEnvironment() {
        // Default implementation - can be overridden
        System.out.println("Cleaning up test environment...");
    }
    
    // Utility methods for test classes
    
    /**
     * Navigate to application URL
     */
    protected void navigateToApplication() {
        String appUrl = config.getApplicationUrl();
        System.out.println("Navigating to application: " + appUrl);
        driverManager.navigateTo(appUrl);
        driverManager.waitForPageLoad();
    }
    
    /**
     * Navigate to specific path within application
     */
    protected void navigateToPath(String path) {
        String fullUrl = config.getApplicationUrl() + (path.startsWith("/") ? path : "/" + path);
        System.out.println("Navigating to path: " + fullUrl);
        driverManager.navigateTo(fullUrl);
        driverManager.waitForPageLoad();
    }
    
    /**
     * Wait for a specified amount of time (in seconds)
     */
    protected void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Wait interrupted: " + e.getMessage());
        }
    }
    
    /**
     * Get test data file path
     */
    protected String getTestDataPath(String fileName) {
        return config.getTestDataPath() + "/" + fileName;
    }
    
    /**
     * Assert test condition with custom message
     */
    protected void assertTest(boolean condition, String message) {
        if (!condition) {
            System.err.println("Assertion Failed: " + message);
            throw new AssertionError(message);
        }
    }
    
    /**
     * Log test step
     */
    protected void logStep(String stepDescription) {
        System.out.println("Step: " + stepDescription);
    }
    
    /**
     * Log test info
     */
    protected void logInfo(String info) {
        System.out.println("Info: " + info);
    }
    
    /**
     * Log test warning
     */
    protected void logWarning(String warning) {
        System.err.println("Warning: " + warning);
    }
}