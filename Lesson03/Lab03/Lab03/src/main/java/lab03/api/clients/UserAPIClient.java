package lab03.api.clients;

import io.restassured.response.Response;
import lab03.api.base.BaseAPIClient;
import lab03.api.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User API Client
 * 
 * This class provides methods for interacting with user-related API endpoints,
 * demonstrating REST API testing patterns and best practices.
 * 
 * Features:
 * - User CRUD operations
 * - Authentication management
 * - Response validation
 * - Error handling
 * - Data model mapping
 */
public class UserAPIClient extends BaseAPIClient {
    
    private static final String USERS_ENDPOINT = "/users";
    private static final String AUTH_ENDPOINT = "/auth";
    private static final String HEALTH_ENDPOINT = "/health";
    private static final String VERSION_ENDPOINT = "/version";
    
    /**
     * Check if API is healthy
     */
    @Override
    public boolean isHealthy() {
        try {
            Response response = get(HEALTH_ENDPOINT);
            return response.getStatusCode() == 200;
        } catch (Exception e) {
            logApiError("Health check failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get API version
     */
    @Override
    public String getApiVersion() {
        try {
            Response response = get(VERSION_ENDPOINT);
            if (response.getStatusCode() == 200) {
                return response.jsonPath().getString("version");
            }
            return "unknown";
        } catch (Exception e) {
            logApiError("Failed to get API version: " + e.getMessage());
            return "unknown";
        }
    }
    
    // Authentication Methods
    
    /**
     * Authenticate user and set auth token
     */
    public boolean authenticateUser(String username, String password) {
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);
        
        try {
            Response response = post(AUTH_ENDPOINT + "/login", credentials);
            validateStatusCode(response, 200);
            
            String token = extractFromResponse(response, "token", String.class);
            setAuthToken(token);
            
            logApiAction("User authenticated successfully: " + username);
            return true;
        } catch (Exception e) {
            logApiError("Authentication failed for user: " + username + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Logout user
     */
    public boolean logoutUser() {
        try {
            Response response = post(AUTH_ENDPOINT + "/logout");
            validateStatusCode(response, 200);
            clearAuthToken();
            
            logApiAction("User logged out successfully");
            return true;
        } catch (Exception e) {
            logApiError("Logout failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Validate current auth token
     */
    public boolean validateToken() {
        try {
            Response response = get(AUTH_ENDPOINT + "/validate");
            return response.getStatusCode() == 200;
        } catch (Exception e) {
            logApiError("Token validation failed: " + e.getMessage());
            return false;
        }
    }
    
    // User CRUD Operations
    
    /**
     * Create a new user
     */
    public User createUser(User user) {
        try {
            Response response = post(USERS_ENDPOINT, user);
            validateStatusCode(response, 201);
            
            User createdUser = response.as(User.class);
            logApiAction("User created successfully with ID: " + createdUser.getId());
            return createdUser;
        } catch (Exception e) {
            logApiError("Failed to create user: " + e.getMessage());
            throw new RuntimeException("User creation failed", e);
        }
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(Long userId) {
        try {
            Response response = get(USERS_ENDPOINT + "/" + userId);
            validateStatusCode(response, 200);
            
            User user = response.as(User.class);
            logApiAction("Retrieved user with ID: " + userId);
            return user;
        } catch (Exception e) {
            logApiError("Failed to get user by ID: " + userId + " - " + e.getMessage());
            throw new RuntimeException("Failed to get user", e);
        }
    }
    
    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        
        try {
            Response response = get(USERS_ENDPOINT + "/search", params);
            validateStatusCode(response, 200);
            
            List<User> users = response.jsonPath().getList("", User.class);
            if (users.isEmpty()) {
                throw new RuntimeException("User not found: " + username);
            }
            
            User user = users.get(0);
            logApiAction("Retrieved user by username: " + username);
            return user;
        } catch (Exception e) {
            logApiError("Failed to get user by username: " + username + " - " + e.getMessage());
            throw new RuntimeException("Failed to get user", e);
        }
    }
    
    /**
     * Get all users with pagination
     */
    public List<User> getAllUsers(int page, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        
        try {
            Response response = get(USERS_ENDPOINT, params);
            validateStatusCode(response, 200);
            
            List<User> users = response.jsonPath().getList("content", User.class);
            logApiAction("Retrieved " + users.size() + " users (page " + page + ", size " + size + ")");
            return users;
        } catch (Exception e) {
            logApiError("Failed to get all users: " + e.getMessage());
            throw new RuntimeException("Failed to get users", e);
        }
    }
    
    /**
     * Get all users (first page with default size)
     */
    public List<User> getAllUsers() {
        return getAllUsers(0, 10);
    }
    
    /**
     * Update user
     */
    public User updateUser(Long userId, User user) {
        try {
            Response response = put(USERS_ENDPOINT + "/" + userId, user);
            validateStatusCode(response, 200);
            
            User updatedUser = response.as(User.class);
            logApiAction("User updated successfully with ID: " + userId);
            return updatedUser;
        } catch (Exception e) {
            logApiError("Failed to update user: " + userId + " - " + e.getMessage());
            throw new RuntimeException("User update failed", e);
        }
    }
    
    /**
     * Partially update user (PATCH)
     */
    public User patchUser(Long userId, Map<String, Object> updates) {
        try {
            Response response = patch(USERS_ENDPOINT + "/" + userId, updates);
            validateStatusCode(response, 200);
            
            User updatedUser = response.as(User.class);
            logApiAction("User partially updated with ID: " + userId);
            return updatedUser;
        } catch (Exception e) {
            logApiError("Failed to patch user: " + userId + " - " + e.getMessage());
            throw new RuntimeException("User patch failed", e);
        }
    }
    
    /**
     * Delete user
     */
    public boolean deleteUser(Long userId) {
        try {
            Response response = delete(USERS_ENDPOINT + "/" + userId);
            validateStatusCode(response, 204);
            
            logApiAction("User deleted successfully with ID: " + userId);
            return true;
        } catch (Exception e) {
            logApiError("Failed to delete user: " + userId + " - " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if user exists
     */
    public boolean userExists(Long userId) {
        try {
            Response response = get(USERS_ENDPOINT + "/" + userId);
            return response.getStatusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if username is available
     */
    public boolean isUsernameAvailable(String username) {
        try {
            Response response = get(USERS_ENDPOINT + "/check-username/" + username);
            validateStatusCode(response, 200);
            
            Boolean available = extractFromResponse(response, "available", Boolean.class);
            logApiAction("Username availability check for '" + username + "': " + available);
            return available;
        } catch (Exception e) {
            logApiError("Failed to check username availability: " + username + " - " + e.getMessage());
            return false;
        }
    }
    
    // User Search and Filter Methods
    
    /**
     * Search users by criteria
     */
    public List<User> searchUsers(Map<String, Object> searchCriteria) {
        try {
            Response response = get(USERS_ENDPOINT + "/search", searchCriteria);
            validateStatusCode(response, 200);
            
            List<User> users = response.jsonPath().getList("", User.class);
            logApiAction("Found " + users.size() + " users matching search criteria");
            return users;
        } catch (Exception e) {
            logApiError("User search failed: " + e.getMessage());
            throw new RuntimeException("User search failed", e);
        }
    }
    
    /**
     * Get users by role
     */
    public List<User> getUsersByRole(String role) {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("role", role);
        return searchUsers(criteria);
    }
    
    /**
     * Get active users
     */
    public List<User> getActiveUsers() {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("active", true);
        return searchUsers(criteria);
    }
    
    // User Validation Methods
    
    /**
     * Validate user data
     */
    public boolean validateUser(User user) {
        return user != null && 
               user.getUsername() != null && !user.getUsername().isEmpty() &&
               user.getEmail() != null && !user.getEmail().isEmpty() &&
               user.getFirstName() != null && !user.getFirstName().isEmpty() &&
               user.getLastName() != null && !user.getLastName().isEmpty();
    }
    
    /**
     * Validate user creation response
     */
    public boolean validateUserCreated(User originalUser, User createdUser) {
        if (!validateUser(createdUser)) {
            return false;
        }
        
        return createdUser.getId() != null &&
               createdUser.getUsername().equals(originalUser.getUsername()) &&
               createdUser.getEmail().equals(originalUser.getEmail()) &&
               createdUser.getFirstName().equals(originalUser.getFirstName()) &&
               createdUser.getLastName().equals(originalUser.getLastName());
    }
    
    /**
     * Get user count
     */
    public long getUserCount() {
        try {
            Response response = get(USERS_ENDPOINT + "/count");
            validateStatusCode(response, 200);
            
            Long count = extractFromResponse(response, "count", Long.class);
            logApiAction("Total user count: " + count);
            return count;
        } catch (Exception e) {
            logApiError("Failed to get user count: " + e.getMessage());
            return 0;
        }
    }
    
    // Bulk Operations
    
    /**
     * Create multiple users
     */
    public List<User> createUsers(List<User> users) {
        try {
            Response response = post(USERS_ENDPOINT + "/batch", users);
            validateStatusCode(response, 201);
            
            List<User> createdUsers = response.jsonPath().getList("", User.class);
            logApiAction("Created " + createdUsers.size() + " users in batch");
            return createdUsers;
        } catch (Exception e) {
            logApiError("Batch user creation failed: " + e.getMessage());
            throw new RuntimeException("Batch user creation failed", e);
        }
    }
    
    /**
     * Delete multiple users
     */
    public boolean deleteUsers(List<Long> userIds) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("userIds", userIds);
            
            Response response = delete(USERS_ENDPOINT + "/batch", request);
            validateStatusCode(response, 204);
            
            logApiAction("Deleted " + userIds.size() + " users in batch");
            return true;
        } catch (Exception e) {
            logApiError("Batch user deletion failed: " + e.getMessage());
            return false;
        }
    }
}