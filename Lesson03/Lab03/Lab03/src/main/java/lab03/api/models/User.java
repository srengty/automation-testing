package lab03.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User Data Model
 * 
 * This class represents a User entity for API testing,
 * demonstrating data model patterns and JSON serialization.
 * 
 * Features:
 * - JSON serialization/deserialization
 * - Validation methods
 * - Builder pattern for object creation
 * - Proper equals/hashCode implementation
 * - String representation for logging
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("firstName")
    private String firstName;
    
    @JsonProperty("lastName")
    private String lastName;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("role")
    private String role;
    
    @JsonProperty("active")
    private Boolean active;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    @JsonProperty("lastLoginAt")
    private LocalDateTime lastLoginAt;
    
    @JsonProperty("profilePicture")
    private String profilePicture;
    
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    
    @JsonProperty("department")
    private String department;
    
    @JsonProperty("jobTitle")
    private String jobTitle;
    
    // Default constructor
    public User() {
        this.active = true; // Default to active
    }
    
    // Constructor with required fields
    public User(String username, String email, String firstName, String lastName) {
        this();
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Constructor with all basic fields
    public User(String username, String email, String firstName, String lastName, String password) {
        this(username, email, firstName, lastName);
        this.password = password;
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }
    
    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
    
    public String getProfilePicture() {
        return profilePicture;
    }
    
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getJobTitle() {
        return jobTitle;
    }
    
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    
    // Utility Methods
    
    /**
     * Get full name
     */
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (firstName != null && !firstName.isEmpty()) {
            fullName.append(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(lastName);
        }
        return fullName.toString();
    }
    
    /**
     * Check if user is active
     */
    public boolean isActive() {
        return active != null && active;
    }
    
    /**
     * Check if user has role
     */
    public boolean hasRole(String role) {
        return this.role != null && this.role.equalsIgnoreCase(role);
    }
    
    /**
     * Check if user is admin
     */
    public boolean isAdmin() {
        return hasRole("admin") || hasRole("administrator");
    }
    
    /**
     * Validate user data
     */
    public boolean isValid() {
        return username != null && !username.isEmpty() &&
               email != null && !email.isEmpty() && isValidEmail(email) &&
               firstName != null && !firstName.isEmpty() &&
               lastName != null && !lastName.isEmpty();
    }
    
    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
    
    /**
     * Get user initials
     */
    public String getInitials() {
        StringBuilder initials = new StringBuilder();
        if (firstName != null && !firstName.isEmpty()) {
            initials.append(firstName.charAt(0));
        }
        if (lastName != null && !lastName.isEmpty()) {
            initials.append(lastName.charAt(0));
        }
        return initials.toString().toUpperCase();
    }
    
    /**
     * Clear sensitive data (password)
     */
    public User withoutSensitiveData() {
        User copy = new User();
        copy.id = this.id;
        copy.username = this.username;
        copy.email = this.email;
        copy.firstName = this.firstName;
        copy.lastName = this.lastName;
        copy.role = this.role;
        copy.active = this.active;
        copy.createdAt = this.createdAt;
        copy.updatedAt = this.updatedAt;
        copy.lastLoginAt = this.lastLoginAt;
        copy.profilePicture = this.profilePicture;
        copy.phoneNumber = this.phoneNumber;
        copy.department = this.department;
        copy.jobTitle = this.jobTitle;
        // Deliberately exclude password
        return copy;
    }
    
    // Builder Pattern
    
    public static UserBuilder builder() {
        return new UserBuilder();
    }
    
    public static class UserBuilder {
        private User user = new User();
        
        public UserBuilder id(Long id) {
            user.id = id;
            return this;
        }
        
        public UserBuilder username(String username) {
            user.username = username;
            return this;
        }
        
        public UserBuilder email(String email) {
            user.email = email;
            return this;
        }
        
        public UserBuilder firstName(String firstName) {
            user.firstName = firstName;
            return this;
        }
        
        public UserBuilder lastName(String lastName) {
            user.lastName = lastName;
            return this;
        }
        
        public UserBuilder password(String password) {
            user.password = password;
            return this;
        }
        
        public UserBuilder role(String role) {
            user.role = role;
            return this;
        }
        
        public UserBuilder active(Boolean active) {
            user.active = active;
            return this;
        }
        
        public UserBuilder phoneNumber(String phoneNumber) {
            user.phoneNumber = phoneNumber;
            return this;
        }
        
        public UserBuilder department(String department) {
            user.department = department;
            return this;
        }
        
        public UserBuilder jobTitle(String jobTitle) {
            user.jobTitle = jobTitle;
            return this;
        }
        
        public UserBuilder profilePicture(String profilePicture) {
            user.profilePicture = profilePicture;
            return this;
        }
        
        public User build() {
            return user;
        }
    }
    
    // equals and hashCode
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
               Objects.equals(username, user.username) &&
               Objects.equals(email, user.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }
    
    // toString
    
    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", role='" + role + '\'' +
               ", active=" + active +
               ", department='" + department + '\'' +
               ", jobTitle='" + jobTitle + '\'' +
               '}';
    }
    
    /**
     * String representation without sensitive data
     */
    public String toStringSecure() {
        return toString(); // Password is not included in toString anyway
    }
    
    /**
     * Create a copy of this user
     */
    public User copy() {
        User copy = new User();
        copy.id = this.id;
        copy.username = this.username;
        copy.email = this.email;
        copy.firstName = this.firstName;
        copy.lastName = this.lastName;
        copy.password = this.password;
        copy.role = this.role;
        copy.active = this.active;
        copy.createdAt = this.createdAt;
        copy.updatedAt = this.updatedAt;
        copy.lastLoginAt = this.lastLoginAt;
        copy.profilePicture = this.profilePicture;
        copy.phoneNumber = this.phoneNumber;
        copy.department = this.department;
        copy.jobTitle = this.jobTitle;
        return copy;
    }
}