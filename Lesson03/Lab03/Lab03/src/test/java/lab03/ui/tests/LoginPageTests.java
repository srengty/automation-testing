package lab03.ui.tests;

import lab03.infrastructure.base.BaseTest;
import lab03.pageobjects.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Login Page Tests
 * 
 * This class demonstrates UI testing using the Page Object Model pattern
 * and the automation framework infrastructure.
 * 
 * Test Categories:
 * - Positive login scenarios
 * - Negative login scenarios
 * - Form validation tests
 * - UI element verification
 * - Accessibility testing
 */
public class LoginPageTests extends BaseTest {
    
    private LoginPage loginPage;
    
    @Test(priority = 1, 
          description = "Verify login page loads correctly")
    public void testLoginPageLoads() {
        logStep("Navigate to login page");
        navigateToPath("/login");
        
        logStep("Initialize login page object");
        loginPage = new LoginPage();
        
        logStep("Verify login page elements are displayed");
        Assert.assertTrue(loginPage.isLoginFormReady(), 
            "Login form should be ready for user input");
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(), 
            "Username field should be visible");
        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(), 
            "Password field should be visible");
        Assert.assertTrue(loginPage.isLoginButtonEnabled(), 
            "Login button should be enabled");
        
        logStep("Verify page title and URL");
        Assert.assertTrue(getCurrentTitle().contains("Login"), 
            "Page title should contain 'Login'");
        Assert.assertTrue(getCurrentUrl().contains("/login"), 
            "URL should contain '/login'");
        
        logInfo("Login page loaded successfully");
    }
    
    @Test(priority = 2, 
          description = "Test successful login with valid credentials",
          dependsOnMethods = "testLoginPageLoads")
    public void testValidLogin() {
        logStep("Navigate to login page");
        navigateToPath("/login");
        loginPage = new LoginPage();
        
        logStep("Enter valid credentials");
        String username = config.getString("test.user.valid.username", "testuser");
        String password = config.getString("test.user.valid.password", "testpass");
        
        loginPage.login(username, password);
        
        logStep("Verify successful login");
        // Wait for navigation to dashboard or home page
        waitForSeconds(2);
        
        // Verify we're no longer on login page
        Assert.assertFalse(getCurrentUrl().contains("/login"), 
            "Should be redirected away from login page after successful login");
        
        // Could verify dashboard elements or success message here
        logInfo("Valid login test completed successfully");
    }
    
    @Test(priority = 3, 
          description = "Test login with invalid credentials")
    public void testInvalidLogin() {
        logStep("Navigate to login page");
        navigateToPath("/login");
        loginPage = new LoginPage();
        
        logStep("Enter invalid credentials");
        loginPage.login("invaliduser", "invalidpass");
        
        logStep("Verify error message is displayed");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), 
            "Error message should be displayed for invalid credentials");
        
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertFalse(errorMessage.isEmpty(), 
            "Error message should not be empty");
        Assert.assertTrue(errorMessage.toLowerCase().contains("invalid") || 
                         errorMessage.toLowerCase().contains("incorrect") ||
                         errorMessage.toLowerCase().contains("wrong"),
            "Error message should indicate invalid credentials");
        
        logStep("Verify still on login page");
        Assert.assertTrue(getCurrentUrl().contains("/login"), 
            "Should remain on login page after failed login");
        
        logInfo("Invalid login test completed - error message displayed correctly");
    }
    
    @Test(priority = 4, 
          description = "Test empty username validation")
    public void testEmptyUsername() {
        logStep("Navigate to login page");
        navigateToPath("/login");
        loginPage = new LoginPage();
        
        logStep("Enter empty username with valid password");
        loginPage.enterUsername("")
                 .enterPassword("validpassword")
                 .clickLoginButton();
        
        logStep("Verify validation error");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed() || 
                         !loginPage.areAllFieldsValid(),
            "Validation error should be shown for empty username");
        
        logInfo("Empty username validation test completed");
    }
    
    @Test(priority = 5, 
          description = "Test empty password validation")
    public void testEmptyPassword() {
        logStep("Navigate to login page");
        navigateToPath("/login");
        loginPage = new LoginPage();
        
        logStep("Enter valid username with empty password");
        loginPage.enterUsername("testuser")
                 .enterPassword("")
                 .clickLoginButton();
        
        logStep("Verify validation error");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed() || 
                         !loginPage.areAllFieldsValid(),
            "Validation error should be shown for empty password");
        
        logInfo("Empty password validation test completed");
    }
    
    @Test(priority = 6, 
          description = "Test both fields empty validation")
    public void testBothFieldsEmpty() {
        logStep("Navigate to login page");
        navigateToPath("/login");
        loginPage = new LoginPage();
        
        logStep("Click login button with empty fields");
        loginPage.clickLoginButton();
        
        logStep("Verify validation error");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed() || 
                         !loginPage.areAllFieldsValid(),
            "Validation error should be shown for empty fields");
        
        logInfo("Both fields empty validation test completed");
    }
    
    @Test(priority = 7, 
          description = "Test remember me functionality")
    public void testRememberMeCheckbox() {
        logStep("Navigate to login page");
        navigateToPath("/login");
        loginPage = new LoginPage();
        
        logStep("Verify remember me checkbox is unchecked by default");
        Assert.assertFalse(loginPage.isRememberMeChecked(), 
            "Remember me checkbox should be unchecked by default");
        
        logStep("Check remember me checkbox");
        loginPage.checkRememberMe();
        Assert.assertTrue(loginPage.isRememberMeChecked(), 
            "Remember me checkbox should be checked after clicking");
        
        logStep("Uncheck remember me checkbox");
        loginPage.uncheckRememberMe();
        Assert.assertFalse(loginPage.isRememberMeChecked(), 
            "Remember me checkbox should be unchecked after clicking again");
        
        logInfo("Remember me checkbox test completed");
    }
    
    @Test(priority = 8, 
          description = "Test form submission with Enter key")
    public void testSubmitWithEnterKey() {
        logStep("Navigate to login page");
        navigateToPath("/login");
        loginPage = new LoginPage();
        
        logStep("Enter credentials and submit with Enter key");
        String username = config.getString("test.user.valid.username", "testuser");
        String password = config.getString("test.user.valid.password", "testpass");
        
        loginPage.enterUsername(username)
                 .enterPassword(password)
                 .submitFormWithEnter();
        
        logStep("Verify form submission");
        waitForSeconds(2);
        
        // Verify navigation occurred (indicating form was submitted)
        Assert.assertNotEquals(getCurrentUrl(), 
            config.getApplicationUrl() + "/login",
            "Form should be submitted when Enter key is pressed");
        
        logInfo("Enter key submission test completed");
    }
    
    @Test(priority = 9, 
          description = "Test forgot password link")
    public void testForgotPasswordLink() {
        logStep("Navigate to login page");
        navigateToPath("/login");
        loginPage = new LoginPage();
        
        logStep("Verify forgot password link is displayed");
        Assert.assertTrue(loginPage.isForgotPasswordLinkDisplayed(), 
            "Forgot password link should be displayed");
        
        logStep("Click forgot password link");
        loginPage.clickForgotPasswordLink();
        
        logStep("Verify navigation to forgot password page");
        waitForSeconds(2);
        Assert.assertTrue(getCurrentUrl().contains("forgot") || getCurrentUrl().contains("reset"),
            "Should navigate to forgot password page");
        
        logInfo("Forgot password link test completed");
    }
    
    @Test(priority = 10, 
          description = "Test sign up link")
    public void testSignUpLink() {
        logStep("Navigate to login page");
        navigateToPath("/login");
        loginPage = new LoginPage();
        
        logStep("Verify sign up link is displayed");
        Assert.assertTrue(loginPage.isSignUpLinkDisplayed(), 
            "Sign up link should be displayed");
        
        logStep("Click sign up link");
        loginPage.clickSignUpLink();
        
        logStep("Verify navigation to sign up page");
        waitForSeconds(2);
        Assert.assertTrue(getCurrentUrl().contains("signup") || 
                         getCurrentUrl().contains("register"),
            "Should navigate to sign up page");
        
        logInfo("Sign up link test completed");
    }
    
    @Test(priority = 11, 
          description = "Test form field clearing functionality")
    public void testFormFieldClearing() {
        logStep("Navigate to login page");
        navigateToPath("/login");
        loginPage = new LoginPage();
        
        logStep("Enter test data in form fields");
        loginPage.enterUsername("testuser")
                 .enterPassword("testpass");
        
        logStep("Verify fields contain data");
        Assert.assertFalse(loginPage.getUsernameFieldValue().isEmpty(), 
            "Username field should contain entered data");
        Assert.assertFalse(loginPage.getPasswordFieldValue().isEmpty(), 
            "Password field should contain entered data");
        
        logStep("Clear all form fields");
        loginPage.clearAllFields();
        
        logStep("Verify fields are cleared");
        Assert.assertTrue(loginPage.getUsernameFieldValue().isEmpty() || 
                         loginPage.getUsernameFieldValue() == null,
            "Username field should be empty after clearing");
        Assert.assertTrue(loginPage.getPasswordFieldValue().isEmpty() || 
                         loginPage.getPasswordFieldValue() == null,
            "Password field should be empty after clearing");
        
        logInfo("Form field clearing test completed");
    }
    
    @Test(priority = 12, 
          description = "Test page title and heading")
    public void testPageTitleAndHeading() {
        logStep("Navigate to login page");
        navigateToPath("/login");
        loginPage = new LoginPage();
        
        logStep("Verify page title");
        String pageTitle = driverManager.getCurrentTitle();
        Assert.assertTrue(pageTitle.toLowerCase().contains("login"), 
            "Page title should contain 'login'");
        
        logStep("Verify page heading");
        String pageHeading = loginPage.getPageHeading();
        Assert.assertTrue(pageHeading.toLowerCase().contains("login") || 
                         pageHeading.toLowerCase().contains("sign in"),
            "Page heading should indicate this is a login page");
        
        logInfo("Page title and heading test completed");
    }
    
    @Test(priority = 13,
          description = "Test login form accessibility")
    public void testLoginFormAccessibility() {
        logStep("Navigate to login page");
        navigateToPath("/login");
        loginPage = new LoginPage();
        
        logStep("Test form navigation with Tab key");
        loginPage.focusOnUsername();
        // Test tab navigation between form elements
        
        logStep("Test form labels and accessibility attributes");
        // Note: In a real implementation, you would check for:
        // - Proper label associations
        // - ARIA attributes
        // - Keyboard navigation
        // - Screen reader compatibility
        
        logInfo("Basic accessibility test completed - enhance for full accessibility testing");
    }
    
    @Override
    protected void setupTest(java.lang.reflect.Method method) {
        logInfo("Setting up test: " + method.getName());
        // Any test-specific setup can go here
    }
    
    @Override
    protected void cleanupTest(org.testng.ITestResult result, java.lang.reflect.Method method) {
        logInfo("Cleaning up test: " + method.getName());
        // Take screenshot if test failed
        if (result.getStatus() == org.testng.ITestResult.FAILURE) {
            if (loginPage != null) {
                loginPage.takeScreenshot(method.getName() + "_failure");
            }
        }
    }
}