package lab03.api.base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lab03.infrastructure.config.ConfigManager;

import java.util.Map;

/**
 * Base API Client for REST-assured Framework
 * 
 * This abstract class provides common functionality for all API clients
 * in the automation framework, implementing REST API testing patterns.
 * 
 * Features:
 * - Common REST API operations (GET, POST, PUT, DELETE)
 * - Request/Response logging
 * - Authentication handling
 * - Base URL configuration
 * - Error handling and validation
 * - JSON/XML support
 * - Custom headers and parameters
 */
public abstract class BaseAPIClient {
    
    protected ConfigManager config;
    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;
    protected String baseUrl;
    protected String authToken;
    
    /**
     * Constructor - Initialize API client with base configuration
     */
    public BaseAPIClient() {
        this.config = ConfigManager.getInstance();
        this.baseUrl = config.getApiBaseUrl();
        setupRestAssuredConfig();
        createBaseRequestSpec();
        createBaseResponseSpec();
    }
    
    /**
     * Setup REST-assured configuration
     */
    private void setupRestAssuredConfig() {
        RestAssuredConfig restConfig = RestAssuredConfig.config()
            .connectionConfig(
                io.restassured.config.ConnectionConfig.connectionConfig()
                    .closeIdleConnectionsAfterEachResponseAfter(30, java.util.concurrent.TimeUnit.SECONDS)
            )
            .httpClientConfig(
                io.restassured.config.HttpClientConfig.httpClientConfig()
                    .setParam("http.socket.timeout", config.getApiTimeout())
                    .setParam("http.connection.timeout", config.getApiTimeout())
            );
        
        RestAssured.config = restConfig;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
    
    /**
     * Create base request specification
     */
    protected void createBaseRequestSpec() {
        RequestSpecBuilder requestBuilder = new RequestSpecBuilder()
            .setBaseUri(baseUrl)
            .setContentType(ContentType.JSON)
            .addHeader("Accept", "application/json")
            .addHeader("User-Agent", "Automation-Framework/1.0");
        
        // Add authentication if configured
        String apiKey = config.getApiKey();
        if (apiKey != null && !apiKey.isEmpty()) {
            requestBuilder.addHeader("X-API-Key", apiKey);
        }
        
        // Add logging filters if debug mode is enabled
        if (config.getBoolean("api.debug.logging", false)) {
            requestBuilder
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter());
        }
        
        requestSpec = requestBuilder.build();
    }
    
    /**
     * Create base response specification
     */
    protected void createBaseResponseSpec() {
        ResponseSpecBuilder responseBuilder = new ResponseSpecBuilder()
            .expectHeader("Content-Type", org.hamcrest.Matchers.containsString("application/json"));
        
        responseSpec = responseBuilder.build();
    }
    
    /**
     * Set authentication token
     */
    public void setAuthToken(String token) {
        this.authToken = token;
        requestSpec = new RequestSpecBuilder()
            .addRequestSpecification(requestSpec)
            .addHeader("Authorization", "Bearer " + token)
            .build();
        
        logApiAction("Set authentication token");
    }
    
    /**
     * Clear authentication token
     */
    public void clearAuthToken() {
        this.authToken = null;
        createBaseRequestSpec(); // Recreate without auth token
        logApiAction("Cleared authentication token");
    }
    
    // HTTP Methods
    
    /**
     * Perform GET request
     */
    protected Response get(String endpoint) {
        logApiAction("GET " + baseUrl + endpoint);
        
        return RestAssured
            .given(requestSpec)
            .when()
            .get(endpoint)
            .then()
            .extract()
            .response();
    }
    
    /**
     * Perform GET request with parameters
     */
    protected Response get(String endpoint, Map<String, Object> parameters) {
        logApiAction("GET " + baseUrl + endpoint + " with parameters: " + parameters);
        
        return RestAssured
            .given(requestSpec)
            .queryParams(parameters)
            .when()
            .get(endpoint)
            .then()
            .extract()
            .response();
    }
    
    /**
     * Perform POST request with body
     */
    protected Response post(String endpoint, Object requestBody) {
        logApiAction("POST " + baseUrl + endpoint);
        
        return RestAssured
            .given(requestSpec)
            .body(requestBody)
            .when()
            .post(endpoint)
            .then()
            .extract()
            .response();
    }
    
    /**
     * Perform POST request without body
     */
    protected Response post(String endpoint) {
        logApiAction("POST " + baseUrl + endpoint);
        
        return RestAssured
            .given(requestSpec)
            .when()
            .post(endpoint)
            .then()
            .extract()
            .response();
    }
    
    /**
     * Perform PUT request with body
     */
    protected Response put(String endpoint, Object requestBody) {
        logApiAction("PUT " + baseUrl + endpoint);
        
        return RestAssured
            .given(requestSpec)
            .body(requestBody)
            .when()
            .put(endpoint)
            .then()
            .extract()
            .response();
    }
    
    /**
     * Perform PATCH request with body
     */
    protected Response patch(String endpoint, Object requestBody) {
        logApiAction("PATCH " + baseUrl + endpoint);
        
        return RestAssured
            .given(requestSpec)
            .body(requestBody)
            .when()
            .patch(endpoint)
            .then()
            .extract()
            .response();
    }
    
    /**
     * Perform DELETE request
     */
    protected Response delete(String endpoint) {
        logApiAction("DELETE " + baseUrl + endpoint);
        
        return RestAssured
            .given(requestSpec)
            .when()
            .delete(endpoint)
            .then()
            .extract()
            .response();
    }
    
    /**
     * Perform DELETE request with body
     */
    protected Response delete(String endpoint, Object requestBody) {
        logApiAction("DELETE " + baseUrl + endpoint);
        
        return RestAssured
            .given(requestSpec)
            .body(requestBody)
            .when()
            .delete(endpoint)
            .then()
            .extract()
            .response();
    }
    
    // Request Customization Methods
    
    /**
     * Add custom header to next request
     */
    protected RequestSpecification addHeader(String name, String value) {
        return RestAssured
            .given(requestSpec)
            .header(name, value);
    }
    
    /**
     * Add custom headers to next request
     */
    protected RequestSpecification addHeaders(Map<String, String> headers) {
        return RestAssured
            .given(requestSpec)
            .headers(headers);
    }
    
    /**
     * Add query parameter to next request
     */
    protected RequestSpecification addQueryParam(String name, Object value) {
        return RestAssured
            .given(requestSpec)
            .queryParam(name, value);
    }
    
    /**
     * Add query parameters to next request
     */
    protected RequestSpecification addQueryParams(Map<String, Object> parameters) {
        return RestAssured
            .given(requestSpec)
            .queryParams(parameters);
    }
    
    /**
     * Set content type for next request
     */
    protected RequestSpecification setContentType(ContentType contentType) {
        return RestAssured
            .given(requestSpec)
            .contentType(contentType);
    }
    
    // Response Validation Methods
    
    /**
     * Validate response status code
     */
    protected void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            logApiError("Status code validation failed. Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode);
            logApiError("Response body: " + response.getBody().asString());
            throw new AssertionError("Expected status code " + expectedStatusCode + " but got " + actualStatusCode);
        }
        logApiAction("Status code validation passed: " + actualStatusCode);
    }
    
    /**
     * Validate response contains expected text
     */
    protected void validateResponseContains(Response response, String expectedText) {
        String responseBody = response.getBody().asString();
        if (!responseBody.contains(expectedText)) {
            logApiError("Response validation failed. Expected to contain: " + expectedText);
            logApiError("Actual response: " + responseBody);
            throw new AssertionError("Response does not contain expected text: " + expectedText);
        }
        logApiAction("Response contains expected text: " + expectedText);
    }
    
    /**
     * Validate JSON response schema (requires JSON schema file)
     */
    protected void validateJsonSchema(Response response, String schemaPath) {
        try {
            response.then().assertThat().body(io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
            logApiAction("JSON schema validation passed: " + schemaPath);
        } catch (Exception e) {
            logApiError("JSON schema validation failed for: " + schemaPath);
            throw new AssertionError("JSON schema validation failed", e);
        }
    }
    
    /**
     * Extract value from JSON response using JSONPath
     */
    protected <T> T extractFromResponse(Response response, String jsonPath, Class<T> type) {
        try {
            T value = response.jsonPath().get(jsonPath);
            logApiAction("Extracted value from JSON path '" + jsonPath + "': " + value);
            return value;
        } catch (Exception e) {
            logApiError("Failed to extract value from JSON path: " + jsonPath);
            throw new RuntimeException("JSON path extraction failed", e);
        }
    }
    
    /**
     * Get response time in milliseconds
     */
    protected long getResponseTime(Response response) {
        return response.getTime();
    }
    
    /**
     * Validate response time is within threshold
     */
    protected void validateResponseTime(Response response, long maxTimeMillis) {
        long actualTime = response.getTime();
        if (actualTime > maxTimeMillis) {
            logApiError("Response time validation failed. Expected: <= " + maxTimeMillis + "ms, Actual: " + actualTime + "ms");
            throw new AssertionError("Response time exceeded threshold: " + actualTime + "ms > " + maxTimeMillis + "ms");
        }
        logApiAction("Response time validation passed: " + actualTime + "ms");
    }
    
    // Utility Methods
    
    /**
     * Log API action
     */
    protected void logApiAction(String action) {
        System.out.println("[API Action] " + action);
    }
    
    /**
     * Log API error
     */
    protected void logApiError(String error) {
        System.err.println("[API Error] " + error);
    }
    
    /**
     * Pretty print response for debugging
     */
    protected void printResponse(Response response) {
        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Time: " + response.getTime() + "ms");
        System.out.println("Response Headers: " + response.getHeaders());
        System.out.println("Response Body: " + response.getBody().asPrettyString());
    }
    
    /**
     * Convert object to JSON string
     */
    protected String toJson(Object object) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
    
    /**
     * Convert JSON string to object
     */
    protected <T> T fromJson(String json, Class<T> clazz) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }
    
    /**
     * Get base URL for this client
     */
    public String getBaseUrl() {
        return baseUrl;
    }
    
    /**
     * Set custom base URL
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        createBaseRequestSpec(); // Recreate with new base URL
        logApiAction("Updated base URL to: " + baseUrl);
    }
    
    /**
     * Check if API is healthy (to be implemented by subclasses)
     */
    public abstract boolean isHealthy();
    
    /**
     * Get API version (to be implemented by subclasses)
     */
    public abstract String getApiVersion();
}