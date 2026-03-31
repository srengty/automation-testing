package lab03.api.tests;

import io.restassured.response.Response;
import lab03.api.clients.UserAPIClient;
import lab03.api.models.User;
import lab03.infrastructure.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User API Tests
 * 
 * This class demonstrates API testing using REST-assured framework
 * and the automation framework infrastructure.
 * 
 * Test Categories:
 * - User CRUD operations
 * - Authentication testing
 * - Data validation
 * - Error handling
 * - Performance testing
 */
public class UserAPITests extends BaseTest {
    
    private UserAPIClient userAPIClient;
    private User testUser;
    private Long createdUserId;
    
    @BeforeClass
    public void setupAPITests() {
        logStep("Initialize API client");
        userAPIClient = new UserAPIClient();
        
        logStep("Verify API health");
        Assert.assertTrue(userAPIClient.isHealthy(), 
            "API should be healthy before running tests");
        
        logStep("Get API version");
        String apiVersion = userAPIClient.getApiVersion();
        logInfo("API Version: " + apiVersion);
        
        logStep("Create test user data");
        testUser = User.builder()
            .username("testuser_" + System.currentTimeMillis())
            .email("testuser_" + System.currentTimeMillis() + "@example.com")
            .firstName("Test")
            .lastName("User")
            .password("TestPassword123!")
            .role("user")
            .department("Engineering")
            .jobTitle("QA Engineer")
            .phoneNumber("+1-555-0123")
            .build();
    }
    
    @Test(priority = 1, 
          description = "Test API health endpoint")
    public void testAPIHealth() {
        logStep("Check API health status");
        boolean isHealthy = userAPIClient.isHealthy();
        
        Assert.assertTrue(isHealthy, "API should be healthy");
        logInfo("API health check passed");
    }
    
    @Test(priority = 2, 
          description = "Test user creation with valid data")
    public void testCreateUser() {
        logStep("Create new user via API");
        User createdUser = userAPIClient.createUser(testUser);
        
        logStep("Validate created user data");
        Assert.assertNotNull(createdUser, "Created user should not be null");
        Assert.assertNotNull(createdUser.getId(), "Created user should have an ID");
        
        // Store for cleanup
        createdUserId = createdUser.getId();
        
        logStep("Verify user data matches input");
        Assert.assertTrue(userAPIClient.validateUserCreated(testUser, createdUser),
            "Created user data should match input data");
        
        Assert.assertEquals(createdUser.getUsername(), testUser.getUsername(),
            "Username should match");
        Assert.assertEquals(createdUser.getEmail(), testUser.getEmail(),
            "Email should match");
        Assert.assertEquals(createdUser.getFirstName(), testUser.getFirstName(),
            "First name should match");
        Assert.assertEquals(createdUser.getLastName(), testUser.getLastName(),
            "Last name should match");
        Assert.assertEquals(createdUser.getRole(), testUser.getRole(),
            "Role should match");
        
        logInfo("User created successfully with ID: " + createdUserId);
    }
    
    @Test(priority = 3, 
          description = "Test get user by ID",
          dependsOnMethods = "testCreateUser")
    public void testGetUserById() {
        logStep("Retrieve user by ID: " + createdUserId);
        User retrievedUser = userAPIClient.getUserById(createdUserId);
        
        logStep("Validate retrieved user data");
        Assert.assertNotNull(retrievedUser, "Retrieved user should not be null");
        Assert.assertEquals(retrievedUser.getId(), createdUserId, 
            "Retrieved user ID should match");
        Assert.assertEquals(retrievedUser.getUsername(), testUser.getUsername(),
            "Username should match");
        Assert.assertEquals(retrievedUser.getEmail(), testUser.getEmail(),
            "Email should match");
        
        logInfo("User retrieved successfully by ID");
    }
    
    @Test(priority = 4, 
          description = "Test get user by username",
          dependsOnMethods = "testCreateUser")
    public void testGetUserByUsername() {
        logStep("Retrieve user by username: " + testUser.getUsername());
        User retrievedUser = userAPIClient.getUserByUsername(testUser.getUsername());
        
        logStep("Validate retrieved user data");
        Assert.assertNotNull(retrievedUser, "Retrieved user should not be null");
        Assert.assertEquals(retrievedUser.getUsername(), testUser.getUsername(),
            "Username should match");
        Assert.assertEquals(retrievedUser.getId(), createdUserId,
            "User ID should match the created user");
        
        logInfo("User retrieved successfully by username");
    }
    
    @Test(priority = 5, 
          description = "Test update user data",
          dependsOnMethods = "testCreateUser")
    public void testUpdateUser() {
        logStep("Update user data");
        User updateData = testUser.copy();
        updateData.setFirstName("Updated First Name");
        updateData.setLastName("Updated Last Name");
        updateData.setJobTitle("Senior QA Engineer");
        
        User updatedUser = userAPIClient.updateUser(createdUserId, updateData);
        
        logStep("Validate updated user data");
        Assert.assertNotNull(updatedUser, "Updated user should not be null");
        Assert.assertEquals(updatedUser.getId(), createdUserId, 
            "User ID should remain the same");
        Assert.assertEquals(updatedUser.getFirstName(), "Updated First Name",
            "First name should be updated");
        Assert.assertEquals(updatedUser.getLastName(), "Updated Last Name",
            "Last name should be updated");
        Assert.assertEquals(updatedUser.getJobTitle(), "Senior QA Engineer",
            "Job title should be updated");
        
        logInfo("User updated successfully");
    }
    
    @Test(priority = 6, 
          description = "Test partial user update using PATCH",
          dependsOnMethods = "testUpdateUser")
    public void testPatchUser() {
        logStep("Partially update user data");
        Map<String, Object> patchData = new HashMap<>();
        patchData.put("department", "Quality Assurance");
        patchData.put("phoneNumber", "+1-555-9999");
        
        User patchedUser = userAPIClient.patchUser(createdUserId, patchData);
        
        logStep("Validate patched user data");
        Assert.assertNotNull(patchedUser, "Patched user should not be null");
        Assert.assertEquals(patchedUser.getDepartment(), "Quality Assurance",
            "Department should be updated");
        Assert.assertEquals(patchedUser.getPhoneNumber(), "+1-555-9999",
            "Phone number should be updated");
        
        logInfo("User patched successfully");
    }
    
    @Test(priority = 7, 
          description = "Test get all users")
    public void testGetAllUsers() {
        logStep("Retrieve all users");
        List<User> users = userAPIClient.getAllUsers();
        
        logStep("Validate users list");
        Assert.assertNotNull(users, "Users list should not be null");
        Assert.assertFalse(users.isEmpty(), "Users list should not be empty");
        
        // Verify our created user is in the list
        boolean foundCreatedUser = users.stream()
            .anyMatch(user -> user.getId().equals(createdUserId));
        Assert.assertTrue(foundCreatedUser, 
            "Created user should be in the users list");
        
        logInfo("Retrieved " + users.size() + " users");
    }
    
    @Test(priority = 8, 
          description = "Test get all users with pagination")
    public void testGetAllUsersWithPagination() {
        logStep("Retrieve users with pagination (page 0, size 5)");
        List<User> users = userAPIClient.getAllUsers(0, 5);
        
        logStep("Validate paginated results");
        Assert.assertNotNull(users, "Users list should not be null");
        Assert.assertTrue(users.size() <= 5, 
            "Page size should be respected");
        
        logInfo("Retrieved " + users.size() + " users from page 0");
    }
    
    @Test(priority = 9, 
          description = "Test user search functionality")
    public void testSearchUsers() {
        logStep("Search users by role");
        List<User> usersByRole = userAPIClient.getUsersByRole("user");
        
        logStep("Validate search results");
        Assert.assertNotNull(usersByRole, "Search results should not be null");
        
        // Verify all returned users have the correct role
        for (User user : usersByRole) {
            Assert.assertEquals(user.getRole(), "user", 
                "All users should have 'user' role");
        }
        
        logInfo("Found " + usersByRole.size() + " users with role 'user'");
    }
    
    @Test(priority = 10, 
          description = "Test get active users")
    public void testGetActiveUsers() {
        logStep("Retrieve active users");
        List<User> activeUsers = userAPIClient.getActiveUsers();
        
        logStep("Validate active users");
        Assert.assertNotNull(activeUsers, "Active users list should not be null");
        
        // Verify all returned users are active
        for (User user : activeUsers) {
            Assert.assertTrue(user.isActive(), 
                "All users should be active");
        }
        
        logInfo("Found " + activeUsers.size() + " active users");
    }
    
    @Test(priority = 11, 
          description = "Test user count endpoint")
    public void testGetUserCount() {
        logStep("Get total user count");
        long userCount = userAPIClient.getUserCount();
        
        logStep("Validate user count");
        Assert.assertTrue(userCount > 0, "User count should be greater than 0");
        
        logInfo("Total user count: " + userCount);
    }
    
    @Test(priority = 12, 
          description = "Test username availability check")
    public void testUsernameAvailability() {
        logStep("Check availability of existing username");
        boolean isExistingUsernameAvailable = userAPIClient.isUsernameAvailable(testUser.getUsername());
        Assert.assertFalse(isExistingUsernameAvailable, 
            "Existing username should not be available");
        
        logStep("Check availability of new username");
        String newUsername = "newuser_" + System.currentTimeMillis();
        boolean isNewUsernameAvailable = userAPIClient.isUsernameAvailable(newUsername);
        Assert.assertTrue(isNewUsernameAvailable, 
            "New username should be available");
        
        logInfo("Username availability check completed");
    }
    
    @Test(priority = 13, 
          description = "Test user existence check",
          dependsOnMethods = "testCreateUser")
    public void testUserExists() {
        logStep("Check if created user exists");
        boolean userExists = userAPIClient.userExists(createdUserId);
        Assert.assertTrue(userExists, "Created user should exist");
        
        logStep("Check if non-existent user exists");
        boolean nonExistentUserExists = userAPIClient.userExists(99999L);
        Assert.assertFalse(nonExistentUserExists, 
            "Non-existent user should not exist");
        
        logInfo("User existence check completed");
    }
    
    @Test(priority = 14, 
          description = "Test create user with invalid data")
    public void testCreateUserWithInvalidData() {
        logStep("Attempt to create user with invalid data");
        User invalidUser = new User();
        // Missing required fields
        
        try {
            userAPIClient.createUser(invalidUser);
            Assert.fail("Creating user with invalid data should throw exception");
        } catch (Exception e) {
            logInfo("Expected exception caught for invalid user data: " + e.getMessage());
        }
    }
    
    @Test(priority = 15, 
          description = "Test get non-existent user")
    public void testGetNonExistentUser() {
        logStep("Attempt to get non-existent user");
        try {
            userAPIClient.getUserById(99999L);
            Assert.fail("Getting non-existent user should throw exception");
        } catch (Exception e) {
            logInfo("Expected exception caught for non-existent user: " + e.getMessage());
        }
    }
    
    @Test(priority = 16, 
          description = "Test authentication with valid credentials")
    public void testAuthenticateUser() {
        logStep("Authenticate with valid credentials");
        // Use admin credentials from config for authentication test
        String username = config.getString("test.admin.username", "admin");
        String password = config.getString("test.admin.password", "admin123");
        
        boolean authResult = userAPIClient.authenticateUser(username, password);
        Assert.assertTrue(authResult, "Authentication should succeed with valid credentials");
        
        logStep("Validate authentication token");
        boolean tokenValid = userAPIClient.validateToken();
        Assert.assertTrue(tokenValid, "Auth token should be valid after authentication");
        
        logInfo("User authentication test completed successfully");
    }
    
    @Test(priority = 17, 
          description = "Test authentication with invalid credentials")
    public void testAuthenticateUserInvalidCredentials() {
        logStep("Attempt authentication with invalid credentials");
        boolean authResult = userAPIClient.authenticateUser("invaliduser", "invalidpass");
        Assert.assertFalse(authResult, 
            "Authentication should fail with invalid credentials");
        
        logInfo("Invalid authentication test completed");
    }
    
    @Test(priority = 18, 
          description = "Test logout functionality",
          dependsOnMethods = "testAuthenticateUser")
    public void testLogoutUser() {
        // First authenticate
        String username = config.getString("test.admin.username", "admin");
        String password = config.getString("test.admin.password", "admin123");
        userAPIClient.authenticateUser(username, password);
        
        logStep("Logout user");
        boolean logoutResult = userAPIClient.logoutUser();
        Assert.assertTrue(logoutResult, "Logout should be successful");
        
        logStep("Validate token is cleared");
        boolean tokenValid = userAPIClient.validateToken();
        Assert.assertFalse(tokenValid, "Token should be invalid after logout");
        
        logInfo("User logout test completed successfully");
    }
    
    @Test(priority = 99, 
          description = "Clean up created test data",
          dependsOnMethods = {"testCreateUser"})
    public void testDeleteUser() {
        if (createdUserId != null) {
            logStep("Delete created test user");
            boolean deleteResult = userAPIClient.deleteUser(createdUserId);
            Assert.assertTrue(deleteResult, "User deletion should be successful");
            
            logStep("Verify user is deleted");
            boolean userExists = userAPIClient.userExists(createdUserId);
            Assert.assertFalse(userExists, "User should not exist after deletion");
            
            logInfo("Test data cleanup completed - user deleted");
        } else {
            logWarning("No user ID to clean up");
        }
    }
    
    @Override
    protected void setupTest(java.lang.reflect.Method method) {
        logInfo("Setting up API test: " + method.getName());
    }
    
    @Override
    protected void cleanupTest(org.testng.ITestResult result, java.lang.reflect.Method method) {
        logInfo("Cleaning up API test: " + method.getName());
        
        // Log response details on failure for debugging
        if (result.getStatus() == org.testng.ITestResult.FAILURE) {
            logApiError("API test failed: " + method.getName());
            if (result.getThrowable() != null) {
                logApiError("Error: " + result.getThrowable().getMessage());
            }
        }
    }
    
    private void logApiError(String error) {
        System.err.println("[API Test Error] " + error);
    }
}