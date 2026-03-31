package lab03.infrastructure.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import lab03.infrastructure.config.ConfigManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * WebDriver Manager for Test Automation Framework
 * 
 * This class manages WebDriver instances with thread-local storage for parallel execution.
 * It supports multiple browsers and provides configuration-based setup.
 * 
 * Features:
 * - Thread-safe WebDriver management
 * - Multi-browser support (Chrome, Firefox, Edge, Safari)
 * - Headless mode support
 * - Automatic driver binary management
 * - Custom browser options and capabilities
 * - Mobile emulation support
 */
public class DriverManager {
    
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> waitThreadLocal = new ThreadLocal<>();
    private final ConfigManager config;
    
    public DriverManager() {
        this.config = ConfigManager.getInstance();
    }
    
    /**
     * Initialize WebDriver based on configuration
     */
    public void initializeDriver() {
        String browser = config.getBrowser().toLowerCase();
        boolean headless = config.isHeadless();
        
        System.out.println("Initializing " + browser + " driver (headless: " + headless + ")");
        
        WebDriver driver = switch (browser) {
            case "chrome" -> createChromeDriver(headless);
            case "firefox" -> createFirefoxDriver(headless);
            case "edge" -> createEdgeDriver(headless);
            case "safari" -> createSafariDriver();
            default -> {
                System.err.println("Unsupported browser: " + browser + ". Using Chrome as default.");
                yield createChromeDriver(headless);
            }
        };
        
        setupDriver(driver);
        driverThreadLocal.set(driver);
        
        // Initialize WebDriverWait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        waitThreadLocal.set(wait);
    }
    
    /**
     * Create Chrome WebDriver with configuration
     */
    private WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        // Basic Chrome options
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        
        // Download preferences
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.managed_default_content_settings.images", 2); // Disable images for faster loading
        options.setExperimentalOption("prefs", prefs);
        
        // Mobile emulation (if configured)
        String mobileDevice = config.getString("mobile.emulation.device", "");
        if (!mobileDevice.isEmpty()) {
            Map<String, String> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", mobileDevice);
            options.setExperimentalOption("mobileEmulation", mobileEmulation);
        }
        
        // Performance logging
        options.setCapability("goog:loggingPrefs", Map.of("performance", "ALL"));
        
        return new ChromeDriver(options);
    }
    
    /**
     * Create Firefox WebDriver with configuration
     */
    private WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        
        FirefoxOptions options = new FirefoxOptions();
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        // Firefox-specific options
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        
        // Set preferences
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("media.volume_scale", "0.0");
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Create Edge WebDriver with configuration
     */
    private WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        
        EdgeOptions options = new EdgeOptions();
        
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        
        return new EdgeDriver(options);
    }
    
    /**
     * Create Safari WebDriver
     * Note: Safari doesn't support headless mode natively
     */
    private WebDriver createSafariDriver() {
        return new SafariDriver();
    }
    
    /**
     * Setup common driver configurations
     */
    private void setupDriver(WebDriver driver) {
        // Set timeouts
        driver.manage().timeouts()
            .implicitlyWait(Duration.ofSeconds(config.getImplicitWait()))
            .pageLoadTimeout(Duration.ofSeconds(config.getPageLoadTimeout()));
        
        // Maximize window (unless mobile emulation is enabled)
        String mobileDevice = config.getString("mobile.emulation.device", "");
        if (mobileDevice.isEmpty()) {
            driver.manage().window().maximize();
        }
        
        // Set custom window size if configured
        String windowSize = config.getString("browser.window.size", "");
        if (!windowSize.isEmpty() && windowSize.contains("x")) {
            String[] dimensions = windowSize.split("x");
            try {
                int width = Integer.parseInt(dimensions[0].trim());
                int height = Integer.parseInt(dimensions[1].trim());
                driver.manage().window().setSize(new Dimension(width, height));
            } catch (NumberFormatException e) {
                System.err.println("Invalid window size format: " + windowSize);
            }
        }
        
        System.out.println("Driver setup completed successfully");
    }
    
    /**
     * Get current thread's WebDriver instance
     */
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Call initializeDriver() first.");
        }
        return driver;
    }
    
    /**
     * Get current thread's WebDriverWait instance
     */
    public static WebDriverWait getWait() {
        WebDriverWait wait = waitThreadLocal.get();
        if (wait == null) {
            throw new IllegalStateException("WebDriverWait not initialized. Call initializeDriver() first.");
        }
        return wait;
    }
    
    /**
     * Check if driver is initialized for current thread
     */
    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }
    
    /**
     * Quit current thread's WebDriver and clean up resources
     */
    public void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("WebDriver quit successfully");
            } catch (Exception e) {
                System.err.println("Error while quitting WebDriver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
                waitThreadLocal.remove();
            }
        }
    }
    
    /**
     * Refresh current page
     */
    public void refreshPage() {
        getDriver().navigate().refresh();
    }
    
    /**
     * Navigate to URL
     */
    public void navigateTo(String url) {
        System.out.println("Navigating to: " + url);
        getDriver().get(url);
    }
    
    /**
     * Navigate back
     */
    public void navigateBack() {
        getDriver().navigate().back();
    }
    
    /**
     * Navigate forward
     */
    public void navigateForward() {
        getDriver().navigate().forward();
    }
    
    /**
     * Get current page title
     */
    public String getCurrentTitle() {
        return getDriver().getTitle();
    }
    
    /**
     * Get current page URL
     */
    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }
    
    /**
     * Switch to new window/tab
     */
    public void switchToNewWindow() {
        String originalWindow = getDriver().getWindowHandle();
        for (String windowHandle : getDriver().getWindowHandles()) {
            if (!originalWindow.equals(windowHandle)) {
                getDriver().switchTo().window(windowHandle);
                break;
            }
        }
    }
    
    /**
     * Close current window and switch back to original
     */
    public void closeCurrentWindowAndSwitchBack() {
        String currentWindow = getDriver().getWindowHandle();
        getDriver().close();
        
        for (String windowHandle : getDriver().getWindowHandles()) {
            if (!currentWindow.equals(windowHandle)) {
                getDriver().switchTo().window(windowHandle);
                break;
            }
        }
    }
    
    /**
     * Execute JavaScript
     */
    public Object executeJavaScript(String script, Object... args) {
        return ((org.openqa.selenium.JavascriptExecutor) getDriver()).executeScript(script, args);
    }
    
    /**
     * Scroll to element
     */
    public void scrollToElement(org.openqa.selenium.WebElement element) {
        executeJavaScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }
    
    /**
     * Wait for page to load completely
     */
    public void waitForPageLoad() {
        getWait().until(driver -> executeJavaScript("return document.readyState").equals("complete"));
    }
    
    /**
     * Add custom capability for driver configuration
     */
    public static class DriverCapabilities {
        public static final String ACCEPT_SSL_CERTS = "acceptSslCerts";
        public static final String ACCEPT_INSECURE_CERTS = "acceptInsecureCerts";
        public static final String BROWSER_VERSION = "browserVersion";
        public static final String PLATFORM_NAME = "platformName";
    }
}