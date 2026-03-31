package lab03.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Configuration Manager for Test Automation Framework
 * 
 * This class provides centralized configuration management for the test automation
 * framework. It supports multiple environments and configuration sources.
 * 
 * Features:
 * - Environment-specific configuration loading
 * - YAML and Properties file support
 * - Default value fallbacks
 * - Type-safe configuration access
 */
public class ConfigManager {
    
    private static ConfigManager instance;
    private Map<String, Object> configData;
    private String currentEnvironment;
    
    private ConfigManager() {
        currentEnvironment = System.getProperty("environment", "local");
        loadConfiguration();
    }
    
    /**
     * Singleton instance getter
     * @return ConfigManager instance
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    /**
     * Load configuration from environment-specific files
     */
    private void loadConfiguration() {
        configData = new HashMap<>();
        
        // Load base configuration
        loadPropertiesFile("/config/base.properties");
        loadYamlFile("/config/base.yml");
        
        // Load environment-specific configuration
        loadPropertiesFile("/config/" + currentEnvironment + ".properties");
        loadYamlFile("/config/" + currentEnvironment + ".yml");
        
        // Override with system properties
        overrideWithSystemProperties();
        
        System.out.println("Loaded configuration for environment: " + currentEnvironment);
    }
    
    /**
     * Load properties file into configuration
     */
    private void loadPropertiesFile(String fileName) {
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                props.forEach((key, value) -> configData.put(key.toString(), value));
                System.out.println("Loaded properties from: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load properties file: " + fileName);
        }
    }
    
    /**
     * Load YAML file into configuration
     */
    private void loadYamlFile(String fileName) {
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            if (is != null) {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                Map<String, Object> yamlData = mapper.readValue(is, Map.class);
                flattenMap("", yamlData, configData);
                System.out.println("Loaded YAML from: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load YAML file: " + fileName);
        }
    }
    
    /**
     * Flatten nested YAML structure with dot notation
     */
    @SuppressWarnings("unchecked")
    private void flattenMap(String prefix, Map<String, Object> map, Map<String, Object> result) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            Object value = entry.getValue();
            
            if (value instanceof Map) {
                flattenMap(key, (Map<String, Object>) value, result);
            } else {
                result.put(key, value);
            }
        }
    }
    
    /**
     * Override configuration with system properties
     */
    private void overrideWithSystemProperties() {
        System.getProperties().forEach((key, value) -> {
            configData.put(key.toString(), value);
        });
    }
    
    /**
     * Get string configuration value
     */
    public String getString(String key, String defaultValue) {
        Object value = configData.get(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    /**
     * Get string configuration value (required)
     */
    public String getString(String key) {
        return getString(key, null);
    }
    
    /**
     * Get integer configuration value
     */
    public int getInt(String key, int defaultValue) {
        Object value = configData.get(key);
        if (value != null) {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException e) {
                System.err.println("Warning: Invalid integer value for key '" + key + "': " + value);
            }
        }
        return defaultValue;
    }
    
    /**
     * Get boolean configuration value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = configData.get(key);
        if (value != null) {
            return Boolean.parseBoolean(value.toString());
        }
        return defaultValue;
    }
    
    /**
     * Get double configuration value
     */
    public double getDouble(String key, double defaultValue) {
        Object value = configData.get(key);
        if (value != null) {
            try {
                return Double.parseDouble(value.toString());
            } catch (NumberFormatException e) {
                System.err.println("Warning: Invalid double value for key '" + key + "': " + value);
            }
        }
        return defaultValue;
    }
    
    /**
     * Check if configuration key exists
     */
    public boolean hasKey(String key) {
        return configData.containsKey(key);
    }
    
    /**
     * Get current environment
     */
    public String getCurrentEnvironment() {
        return currentEnvironment;
    }
    
    /**
     * Get all configuration keys starting with prefix
     */
    public Map<String, Object> getKeysWithPrefix(String prefix) {
        Map<String, Object> result = new HashMap<>();
        configData.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(prefix))
            .forEach(entry -> result.put(
                entry.getKey().substring(prefix.length()), 
                entry.getValue()
            ));
        return result;
    }
    
    /**
     * Browser Configuration
     */
    public String getBrowser() {
        return getString("browser", "chrome");
    }
    
    public boolean isHeadless() {
        return getBoolean("headless", false);
    }
    
    public int getImplicitWait() {
        return getInt("selenium.implicit.wait", 10);
    }
    
    public int getExplicitWait() {
        return getInt("selenium.explicit.wait", 30);
    }
    
    public int getPageLoadTimeout() {
        return getInt("selenium.page.load.timeout", 60);
    }
    
    /**
     * Application URLs
     */
    public String getApplicationUrl() {
        return getString("app.url", "http://localhost:3000");
    }
    
    public String getApiBaseUrl() {
        return getString("api.base.url", "http://localhost:8080/api");
    }
    
    /**
     * Test Data Configuration
     */
    public String getTestDataPath() {
        return getString("test.data.path", "src/test/resources/testdata");
    }
    
    public String getScreenshotPath() {
        return getString("screenshot.path", "target/screenshots");
    }
    
    /**
     * Database Configuration
     */
    public String getDatabaseUrl() {
        return getString("database.url");
    }
    
    public String getDatabaseUser() {
        return getString("database.user");
    }
    
    public String getDatabasePassword() {
        return getString("database.password");
    }
    
    /**
     * API Configuration
     */
    public String getApiKey() {
        return getString("api.key");
    }
    
    public int getApiTimeout() {
        return getInt("api.timeout", 30000);
    }
    
    /**
     * Mobile Configuration
     */
    public String getAppiumServerUrl() {
        return getString("appium.server.url", "http://localhost:4723");
    }
    
    public String getMobileApp() {
        return getString("mobile.app.path");
    }
    
    public String getMobilePlatform() {
        return getString("mobile.platform", "Android");
    }
    
    /**
     * Parallel Execution Configuration
     */
    public int getParallelThreads() {
        return getInt("parallel.threads", 3);
    }
    
    public boolean isParallelEnabled() {
        return getBoolean("parallel.enabled", true);
    }
    
    /**
     * Debug configuration to display all loaded configuration
     */
    public void printConfiguration() {
        System.out.println("=== Configuration Debug ===");
        configData.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> System.out.println(entry.getKey() + " = " + entry.getValue()));
        System.out.println("=== End Configuration ===");
    }
}