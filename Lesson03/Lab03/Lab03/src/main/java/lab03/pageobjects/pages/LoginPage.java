package lab03.pageobjects.pages;

import lab03.pageobjects.base.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Login Page Object
 * 
 * This page object represents the login page of the application,
 * demonstrating the Page Object Model pattern implementation.
 * 
 * Features:
 * - Encapsulation of login page elements
 * - Login functionality methods
 * - Page validation
 * - Error handling
 * - Fluent interface for test readability
 */
public class LoginPage extends BasePage {
    
    // Page Elements using @FindBy annotations
    @FindBy(id = "username")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "loginButton")
    private WebElement loginButton;
    
    @FindBy(id = "forgotPasswordLink")
    private WebElement forgotPasswordLink;
    
    @FindBy(id = "signUpLink")
    private WebElement signUpLink;
    
    @FindBy(css = ".error-message")
    private WebElement errorMessage;
    
    @FindBy(css = ".success-message")
    private WebElement successMessage;
    
    @FindBy(id = "rememberMe")
    private WebElement rememberMeCheckbox;
    
    @FindBy(css = ".loading-spinner")
    private WebElement loadingSpinner;
    
    @FindBy(css = ".login-form")
    private WebElement loginForm;
    
    @FindBy(css = ".page-title")
    private WebElement pageTitle;
    
    // Page Constants
    private static final String EXPECTED_TITLE = "Login";
    private static final String EXPECTED_URL_PATH = "/login";
    
    /**
     * Constructor
     */
    public LoginPage() {
        super();
    }
    
    /**
     * Verify that the login page is loaded correctly
     */
    @Override
    protected void verifyPageLoaded() {
        try {
            waitForElementVisible(loginForm);
            waitForElementVisible(usernameField);
            waitForElementVisible(passwordField);
            waitForElementVisible(loginButton);
            
            // Verify page title
            if (!getPageTitle().contains(EXPECTED_TITLE)) {
                throw new RuntimeException("Login page title validation failed. Expected to contain: " + EXPECTED_TITLE);
            }
            
            // Verify URL contains expected path
            if (!getCurrentUrl().contains(EXPECTED_URL_PATH)) {
                throw new RuntimeException("Login page URL validation failed. Expected to contain: " + EXPECTED_URL_PATH);
            }
            
            logAction("Login page loaded and verified successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify login page is loaded", e);
        }
    }
    
    /**
     * Get page URL for this page
     */
    @Override
    protected String getPageUrl() {
        return "/login";
    }
    
    // Page Action Methods
    
    /**
     * Enter username
     */
    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        return this;
    }
    
    /**
     * Enter password
     */
    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }
    
    /**
     * Click login button
     */
    public void clickLoginButton() {
        click(loginButton);
        // Wait for loading to complete
        waitForLoadingToComplete();
    }
    
    /**
     * Perform complete login with username and password
     */
    public void login(String username, String password) {
        logAction("Performing login with username: " + username);
        enterUsername(username)
            .enterPassword(password)
            .clickLoginButton();
    }
    
    /**
     * Perform login with remember me option
     */
    public void loginWithRememberMe(String username, String password) {
        logAction("Performing login with remember me enabled");
        enterUsername(username)
            .enterPassword(password)
            .checkRememberMe()
            .clickLoginButton();
    }
    
    /**
     * Check remember me checkbox
     */
    public LoginPage checkRememberMe() {
        if (!isSelected(rememberMeCheckbox)) {
            click(rememberMeCheckbox);
        }
        return this;
    }
    
    /**
     * Uncheck remember me checkbox
     */
    public LoginPage uncheckRememberMe() {
        if (isSelected(rememberMeCheckbox)) {
            click(rememberMeCheckbox);
        }
        return this;
    }
    
    /**
     * Click forgot password link
     */
    public void clickForgotPasswordLink() {
        click(forgotPasswordLink);
    }
    
    /**
     * Click sign up link
     */
    public void clickSignUpLink() {
        click(signUpLink);
    }
    
    // Validation Methods
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
    
    /**
     * Get error message text
     */
    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            return getText(errorMessage);
        }
        return "";
    }
    
    /**
     * Check if success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(successMessage);
    }
    
    /**
     * Get success message text
     */
    public String getSuccessMessage() {
        if (isSuccessMessageDisplayed()) {
            return getText(successMessage);
        }
        return "";
    }
    
    /**
     * Check if login button is enabled
     */
    public boolean isLoginButtonEnabled() {
        return isEnabled(loginButton);
    }
    
    /**
     * Check if username field is displayed
     */
    public boolean isUsernameFieldDisplayed() {
        return isDisplayed(usernameField);
    }
    
    /**
     * Check if password field is displayed
     */
    public boolean isPasswordFieldDisplayed() {
        return isDisplayed(passwordField);
    }
    
    /**
     * Check if remember me checkbox is checked
     */
    public boolean isRememberMeChecked() {
        return isSelected(rememberMeCheckbox);
    }
    
    /**
     * Get username field value
     */
    public String getUsernameFieldValue() {
        return getAttribute(usernameField, "value");
    }
    
    /**
     * Get password field value
     */
    public String getPasswordFieldValue() {
        return getAttribute(passwordField, "value");
    }
    
    /**
     * Clear username field
     */
    public LoginPage clearUsername() {
        waitForElementVisible(usernameField);
        usernameField.clear();
        return this;
    }
    
    /**
     * Clear password field
     */
    public LoginPage clearPassword() {
        waitForElementVisible(passwordField);
        passwordField.clear();
        return this;
    }
    
    /**
     * Clear all form fields
     */
    public LoginPage clearAllFields() {
        clearUsername();
        clearPassword();
        if (isRememberMeChecked()) {
            uncheckRememberMe();
        }
        return this;
    }
    
    // Utility Methods
    
    /**
     * Wait for loading spinner to disappear
     */
    public void waitForLoadingToComplete() {
        try {
            if (isDisplayed(loadingSpinner)) {
                waitForElementToDisappear(loadingSpinner);
                logAction("Loading completed");
            }
        } catch (Exception e) {
            // Loading spinner might not be present, ignore
        }
    }
    
    /**
     * Validate login form is ready for input
     */
    public boolean isLoginFormReady() {
        return isDisplayed(usernameField) && 
               isDisplayed(passwordField) && 
               isDisplayed(loginButton) && 
               isEnabled(usernameField) && 
               isEnabled(passwordField) && 
               isEnabled(loginButton);
    }
    
    /**
     * Get page heading text
     */
    public String getPageHeading() {
        return getText(pageTitle);
    }
    
    /**
     * Check if forgot password link is displayed
     */
    public boolean isForgotPasswordLinkDisplayed() {
        return isDisplayed(forgotPasswordLink);
    }
    
    /**
     * Check if sign up link is displayed
     */
    public boolean isSignUpLinkDisplayed() {
        return isDisplayed(signUpLink);
    }
    
    /**
     * Focus on username field
     */
    public LoginPage focusOnUsername() {
        click(usernameField);
        return this;
    }
    
    /**
     * Focus on password field
     */
    public LoginPage focusOnPassword() {
        click(passwordField);
        return this;
    }
    
    /**
     * Submit form using Enter key
     */
    public void submitFormWithEnter() {
        sendKeys(passwordField, org.openqa.selenium.Keys.ENTER);
        waitForLoadingToComplete();
    }
    
    /**
     * Validate specific error message
     */
    public boolean validateErrorMessage(String expectedMessage) {
        if (isErrorMessageDisplayed()) {
            String actualMessage = getErrorMessage();
            return actualMessage.contains(expectedMessage);
        }
        return false;
    }
    
    /**
     * Get all form field validation states
     */
    public boolean areAllFieldsValid() {
        // Check for any validation error classes or attributes
        String usernameClass = getAttribute(usernameField, "class");
        String passwordClass = getAttribute(passwordField, "class");
        
        return !usernameClass.contains("error") && 
               !passwordClass.contains("error") && 
               !usernameClass.contains("invalid") && 
               !passwordClass.contains("invalid");
    }
    
    /**
     * Take screenshot of login page
     */
    public String takeScreenshot(String fileName) {
        return lab03.infrastructure.utils.ScreenshotUtils.captureScreenshot("LoginPage_" + fileName);
    }
}