package lab03.infrastructure.utils;

import lab03.infrastructure.config.ConfigManager;
import lab03.infrastructure.driver.DriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Screenshot Utilities for Test Automation Framework
 * 
 * This utility class provides methods for capturing and managing screenshots
 * during test execution for debugging and reporting purposes.
 * 
 * Features:
 * - Full page screenshots
 * - Element-specific screenshots
 * - Automatic file naming with timestamps
 * - Directory management
 * - Multiple file formats support
 * - Base64 encoding for reports
 */
public class ScreenshotUtils {
    
    private static final ConfigManager config = ConfigManager.getInstance();
    private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd_HH-mm-ss-SSS";
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);
    
    /**
     * Capture full page screenshot with automatic naming
     * 
     * @param testName Name of the test for screenshot naming
     * @return Path to the saved screenshot file
     */
    public static String captureScreenshot(String testName) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String fileName = String.format("%s_%s.png", testName, timestamp);
        return captureScreenshot(fileName);
    }
    
    /**
     * Capture full page screenshot with custom filename
     * 
     * @param fileName Custom filename for the screenshot
     * @return Path to the saved screenshot file
     */
    public static String captureScreenshot(String fileName) {
        try {
            WebDriver driver = DriverManager.getDriver();
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            
            String screenshotDir = config.getScreenshotPath();
            File destFile = new File(screenshotDir, fileName);
            
            // Create directory if it doesn't exist
            createDirectoryIfNotExists(screenshotDir);
            
            // Copy screenshot to destination
            FileUtils.copyFile(sourceFile, destFile);
            
            String absolutePath = destFile.getAbsolutePath();
            System.out.println("Screenshot captured: " + absolutePath);
            return absolutePath;
            
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Capture screenshot of specific element
     * 
     * @param element WebElement to capture
     * @param fileName Custom filename for the screenshot
     * @return Path to the saved screenshot file
     */
    public static String captureElementScreenshot(WebElement element, String fileName) {
        try {
            File sourceFile = element.getScreenshotAs(OutputType.FILE);
            
            String screenshotDir = config.getScreenshotPath();
            File destFile = new File(screenshotDir, fileName);
            
            // Create directory if it doesn't exist
            createDirectoryIfNotExists(screenshotDir);
            
            // Copy screenshot to destination
            FileUtils.copyFile(sourceFile, destFile);
            
            String absolutePath = destFile.getAbsolutePath();
            System.out.println("Element screenshot captured: " + absolutePath);
            return absolutePath;
            
        } catch (Exception e) {
            System.err.println("Failed to capture element screenshot: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Capture screenshot and return as Base64 string
     * 
     * @return Base64 encoded screenshot string
     */
    public static String captureScreenshotAsBase64() {
        try {
            WebDriver driver = DriverManager.getDriver();
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            return takesScreenshot.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot as Base64: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Capture element screenshot and return as Base64 string
     * 
     * @param element WebElement to capture
     * @return Base64 encoded screenshot string
     */
    public static String captureElementScreenshotAsBase64(WebElement element) {
        try {
            return element.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            System.err.println("Failed to capture element screenshot as Base64: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Capture screenshot with custom directory
     * 
     * @param fileName Filename for the screenshot
     * @param customDirectory Custom directory path
     * @return Path to the saved screenshot file
     */
    public static String captureScreenshot(String fileName, String customDirectory) {
        try {
            WebDriver driver = DriverManager.getDriver();
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            
            File destFile = new File(customDirectory, fileName);
            
            // Create directory if it doesn't exist
            createDirectoryIfNotExists(customDirectory);
            
            // Copy screenshot to destination
            FileUtils.copyFile(sourceFile, destFile);
            
            String absolutePath = destFile.getAbsolutePath();
            System.out.println("Screenshot captured to custom directory: " + absolutePath);
            return absolutePath;
            
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot to custom directory: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Capture screenshot with timestamp for test step
     * 
     * @param testName Test name
     * @param stepName Step name
     * @return Path to the saved screenshot file
     */
    public static String captureStepScreenshot(String testName, String stepName) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String fileName = String.format("%s_%s_%s.png", testName, stepName, timestamp);
        return captureScreenshot(fileName);
    }
    
    /**
     * Create directory if it doesn't exist
     * 
     * @param directoryPath Path to the directory
     */
    private static void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Created screenshot directory: " + directoryPath);
            } else {
                System.err.println("Failed to create screenshot directory: " + directoryPath);
            }
        }
    }
    
    /**
     * Clean up old screenshot files (older than specified days)
     * 
     * @param daysToKeep Number of days to keep screenshots
     */
    public static void cleanupOldScreenshots(int daysToKeep) {
        try {
            String screenshotDir = config.getScreenshotPath();
            File directory = new File(screenshotDir);
            
            if (!directory.exists()) {
                return;
            }
            
            long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24L * 60L * 60L * 1000L);
            
            File[] files = directory.listFiles();
            if (files != null) {
                int deletedCount = 0;
                for (File file : files) {
                    if (file.isFile() && file.lastModified() < cutoffTime) {
                        if (file.delete()) {
                            deletedCount++;
                        }
                    }
                }
                System.out.println("Cleaned up " + deletedCount + " old screenshot files");
            }
        } catch (Exception e) {
            System.err.println("Failed to cleanup old screenshots: " + e.getMessage());
        }
    }
    
    /**
     * Get screenshot file size in KB
     * 
     * @param filePath Path to the screenshot file
     * @return File size in KB
     */
    public static long getScreenshotFileSize(String filePath) {
        try {
            File file = new File(filePath);
            return file.length() / 1024; // Convert to KB
        } catch (Exception e) {
            System.err.println("Failed to get screenshot file size: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Generate screenshot filename with test context
     * 
     * @param testClass Test class name
     * @param testMethod Test method name
     * @param status Test status (PASS/FAIL/SKIP)
     * @return Generated filename
     */
    public static String generateScreenshotFileName(String testClass, String testMethod, String status) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        return String.format("%s_%s_%s_%s.png", testClass, testMethod, status, timestamp);
    }
    
    /**
     * Capture comparison screenshots for visual testing
     * 
     * @param testName Test name
     * @param baseline Baseline screenshot name
     * @param current Current screenshot name
     * @return Array of paths [baseline, current]
     */
    public static String[] captureComparisonScreenshots(String testName, String baseline, String current) {
        String baselinePath = captureScreenshot(baseline);
        String currentPath = captureScreenshot(current);
        return new String[]{baselinePath, currentPath};
    }
    
    /**
     * Attach screenshot to Allure report
     * 
     * @param name Attachment name
     * @param filePath Path to screenshot file
     */
    public static void attachToAllureReport(String name, String filePath) {
        try {
            byte[] screenshot = FileUtils.readFileToByteArray(new File(filePath));
            io.qameta.allure.Allure.addAttachment(name, "image/png", 
                new java.io.ByteArrayInputStream(screenshot), "png");
        } catch (IOException e) {
            System.err.println("Failed to attach screenshot to Allure report: " + e.getMessage());
        }
    }
    
    /**
     * Capture screenshot with custom quality settings
     * 
     * @param fileName Filename for the screenshot
     * @param quality Quality setting (0.0 to 1.0)
     * @return Path to the saved screenshot file
     */
    public static String captureScreenshotWithQuality(String fileName, float quality) {
        // This method would need additional implementation for quality control
        // For now, it falls back to standard screenshot capture
        return captureScreenshot(fileName);
    }
}