package lab03.pageobjects.base;

import lab03.infrastructure.driver.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Base Page Object Model Class
 * 
 * This abstract class provides common functionality for all page objects
 * in the automation framework, implementing the Page Object Model pattern.
 * 
 * Features:
 * - Common web element interactions
 * - Wait strategies and conditions
 * - Page validation methods
 * - JavaScript execution utilities
 * - Action chains support
 * - Error handling and logging
 */
public abstract class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    
    // Common timeout values
    protected static final int DEFAULT_TIMEOUT = 30;
    protected static final int SHORT_TIMEOUT = 5;
    protected static final int LONG_TIMEOUT = 60;
    
    /**
     * Constructor - initializes page object
     */
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = DriverManager.getWait();
        this.actions = new Actions(driver);
        
        // Initialize @FindBy elements
        PageFactory.initElements(driver, this);
        
        // Verify page is loaded
        if (shouldVerifyPage()) {
            verifyPageLoaded();
        }
    }
    
    /**
     * Abstract method for page verification
     * Each page object should implement this to verify the page is correctly loaded
     */
    protected abstract void verifyPageLoaded();
    
    /**
     * Abstract method for getting page URL
     * Each page object should implement this to return the page URL
     */
    protected abstract String getPageUrl();
    
    /**
     * Override this method to disable automatic page verification
     */
    protected boolean shouldVerifyPage() {
        return true;
    }
    
    // Wait Strategies
    
    /**
     * Wait for element to be visible
     */
    protected WebElement waitForElementVisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element not visible within timeout: " + element);
        }
    }
    
    /**
     * Wait for element to be visible with custom timeout
     */
    protected WebElement waitForElementVisible(WebElement element, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            return customWait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element not visible within " + timeoutSeconds + " seconds: " + element);
        }
    }
    
    /**
     * Wait for element to be clickable
     */
    protected WebElement waitForElementClickable(WebElement element) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element not clickable within timeout: " + element);
        }
    }
    
    /**
     * Wait for element to be present
     */
    protected WebElement waitForElementPresent(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element not present within timeout: " + locator);
        }
    }
    
    /**
     * Wait for text to be present in element
     */
    protected boolean waitForTextToBePresentInElement(WebElement element, String text) {
        try {
            return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
        } catch (TimeoutException e) {
            throw new TimeoutException("Text '" + text + "' not present in element within timeout");
        }
    }
    
    /**
     * Wait for element to disappear
     */
    protected boolean waitForElementToDisappear(WebElement element) {
        try {
            return wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element did not disappear within timeout: " + element);
        }
    }
    
    /**
     * Wait for page title to contain text
     */
    protected boolean waitForTitleContains(String title) {
        try {
            return wait.until(ExpectedConditions.titleContains(title));
        } catch (TimeoutException e) {
            throw new TimeoutException("Page title does not contain '" + title + "' within timeout");
        }
    }
    
    // Element Interaction Methods
    
    /**
     * Click element with wait
     */
    protected void click(WebElement element) {
        try {
            waitForElementClickable(element);
            scrollToElement(element);
            element.click();
            logAction("Clicked element: " + getElementDescription(element));
        } catch (Exception e) {
            throw new RuntimeException("Failed to click element: " + getElementDescription(element), e);
        }
    }
    
    /**
     * Click element using JavaScript
     */
    protected void clickUsingJavaScript(WebElement element) {
        try {
            waitForElementVisible(element);
            scrollToElement(element);
            executeJavaScript("arguments[0].click();", element);
            logAction("Clicked element using JavaScript: " + getElementDescription(element));
        } catch (Exception e) {
            throw new RuntimeException("Failed to click element using JavaScript: " + getElementDescription(element), e);
        }
    }
    
    /**
     * Double click element
     */
    protected void doubleClick(WebElement element) {
        try {
            waitForElementVisible(element);
            scrollToElement(element);
            actions.doubleClick(element).perform();
            logAction("Double clicked element: " + getElementDescription(element));
        } catch (Exception e) {
            throw new RuntimeException("Failed to double click element: " + getElementDescription(element), e);
        }
    }
    
    /**
     * Right click element
     */
    protected void rightClick(WebElement element) {
        try {
            waitForElementVisible(element);
            scrollToElement(element);
            actions.contextClick(element).perform();
            logAction("Right clicked element: " + getElementDescription(element));
        } catch (Exception e) {
            throw new RuntimeException("Failed to right click element: " + getElementDescription(element), e);
        }
    }
    
    /**
     * Type text into element
     */
    protected void type(WebElement element, String text) {
        try {
            waitForElementVisible(element);
            scrollToElement(element);
            element.clear();
            element.sendKeys(text);
            logAction("Typed '" + text + "' into element: " + getElementDescription(element));
        } catch (Exception e) {
            throw new RuntimeException("Failed to type into element: " + getElementDescription(element), e);
        }
    }
    
    /**
     * Type text without clearing existing content
     */
    protected void typeWithoutClear(WebElement element, String text) {
        try {
            waitForElementVisible(element);
            scrollToElement(element);
            element.sendKeys(text);
            logAction("Typed '" + text + "' into element (without clearing): " + getElementDescription(element));
        } catch (Exception e) {
            throw new RuntimeException("Failed to type into element: " + getElementDescription(element), e);
        }
    }
    
    /**
     * Get element text
     */
    protected String getText(WebElement element) {
        try {
            waitForElementVisible(element);
            String text = element.getText();
            logAction("Retrieved text '" + text + "' from element: " + getElementDescription(element));
            return text;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get text from element: " + getElementDescription(element), e);
        }
    }
    
    /**
     * Get element attribute value
     */
    protected String getAttribute(WebElement element, String attributeName) {
        try {
            waitForElementVisible(element);
            String value = element.getAttribute(attributeName);
            logAction("Retrieved attribute '" + attributeName + "' = '" + value + "' from element: " + getElementDescription(element));
            return value;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get attribute '" + attributeName + "' from element: " + getElementDescription(element), e);
        }
    }
    
    /**
     * Select dropdown option by visible text
     */
    protected void selectByText(WebElement selectElement, String text) {
        try {
            waitForElementVisible(selectElement);
            Select select = new Select(selectElement);
            select.selectByVisibleText(text);
            logAction("Selected option '" + text + "' from dropdown: " + getElementDescription(selectElement));
        } catch (Exception e) {
            throw new RuntimeException("Failed to select option '" + text + "' from dropdown: " + getElementDescription(selectElement), e);
        }
    }
    
    /**
     * Select dropdown option by value
     */
    protected void selectByValue(WebElement selectElement, String value) {
        try {
            waitForElementVisible(selectElement);
            Select select = new Select(selectElement);
            select.selectByValue(value);
            logAction("Selected option with value '" + value + "' from dropdown: " + getElementDescription(selectElement));
        } catch (Exception e) {
            throw new RuntimeException("Failed to select option with value '" + value + "' from dropdown: " + getElementDescription(selectElement), e);
        }
    }
    
    /**
     * Check if element is displayed
     */
    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
    
    /**
     * Check if element is enabled
     */
    protected boolean isEnabled(WebElement element) {
        try {
            waitForElementVisible(element);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if checkbox/radio button is selected
     */
    protected boolean isSelected(WebElement element) {
        try {
            waitForElementVisible(element);
            return element.isSelected();
        } catch (Exception e) {
            return false;
        }
    }
    
    // Utility Methods
    
    /**
     * Scroll to element
     */
    protected void scrollToElement(WebElement element) {
        try {
            executeJavaScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            // Wait a moment for smooth scrolling to complete
            Thread.sleep(500);
        } catch (Exception e) {
            System.err.println("Failed to scroll to element: " + e.getMessage());
        }
    }
    
    /**
     * Scroll to top of page
     */
    protected void scrollToTop() {
        executeJavaScript("window.scrollTo({top: 0, behavior: 'smooth'});");
    }
    
    /**
     * Scroll to bottom of page
     */
    protected void scrollToBottom() {
        executeJavaScript("window.scrollTo({top: document.body.scrollHeight, behavior: 'smooth'});");
    }
    
    /**
     * Execute JavaScript
     */
    protected Object executeJavaScript(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }
    
    /**
     * Refresh current page
     */
    protected void refreshPage() {
        driver.navigate().refresh();
        logAction("Refreshed page");
    }
    
    /**
     * Navigate to URL
     */
    protected void navigateTo(String url) {
        driver.get(url);
        logAction("Navigated to: " + url);
    }
    
    /**
     * Get current page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Get current page URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Wait for page to load completely
     */
    protected void waitForPageLoad() {
        wait.until(driver -> executeJavaScript("return document.readyState").equals("complete"));
    }
    
    /**
     * Hover over element
     */
    protected void hover(WebElement element) {
        try {
            waitForElementVisible(element);
            actions.moveToElement(element).perform();
            logAction("Hovered over element: " + getElementDescription(element));
        } catch (Exception e) {
            throw new RuntimeException("Failed to hover over element: " + getElementDescription(element), e);
        }
    }
    
    /**
     * Drag and drop elements
     */
    protected void dragAndDrop(WebElement source, WebElement target) {
        try {
            waitForElementVisible(source);
            waitForElementVisible(target);
            actions.dragAndDrop(source, target).perform();
            logAction("Dragged element from " + getElementDescription(source) + " to " + getElementDescription(target));
        } catch (Exception e) {
            throw new RuntimeException("Failed to drag and drop elements", e);
        }
    }
    
    /**
     * Send keys to element
     */
    protected void sendKeys(WebElement element, Keys keys) {
        try {
            waitForElementVisible(element);
            element.sendKeys(keys);
            logAction("Sent keys '" + keys + "' to element: " + getElementDescription(element));
        } catch (Exception e) {
            throw new RuntimeException("Failed to send keys to element: " + getElementDescription(element), e);
        }
    }
    
    /**
     * Get element description for logging
     */
    private String getElementDescription(WebElement element) {
        try {
            String tagName = element.getTagName();
            String id = element.getAttribute("id");
            String className = element.getAttribute("class");
            
            StringBuilder description = new StringBuilder(tagName);
            if (id != null && !id.isEmpty()) {
                description.append("[id='").append(id).append("']");
            }
            if (className != null && !className.isEmpty()) {
                description.append("[class='").append(className).append("']");
            }
            
            return description.toString();
        } catch (Exception e) {
            return "Unknown element";
        }
    }
    
    /**
     * Log action for debugging
     */
    protected void logAction(String action) {
        System.out.println("[Page Action] " + action);
    }
    
    /**
     * Find elements by locator
     */
    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }
    
    /**
     * Find element by locator
     */
    protected WebElement findElement(By locator) {
        return waitForElementPresent(locator);
    }
}