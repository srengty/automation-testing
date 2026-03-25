# Practical Examples and Case Studies
## Blackbox and Whitebox Testing

---

## Example 1: E-commerce Shopping Cart

### Business Requirements:
- Users can add items to cart (max 10 different items)
- Apply discount codes (10%, 20%, or $5 off)
- Calculate shipping ($5 standard, $15 express, free over $100)
- Maximum order value: $10,000
- Minimum order value: $1

### Blackbox Testing Approach:

#### Equivalence Partitions:
| Feature | Valid Classes | Invalid Classes |
|---------|---------------|-----------------|
| Item Count | 1-10 items | 0 items, >10 items |
| Discount Code | Valid codes | Invalid codes, expired |
| Order Value | $1-$10,000 | <$1, >$10,000 |
| Shipping | Standard/Express | Invalid option |

#### Test Cases:
```
Test 1: Add single item ($50) + 10% discount + standard shipping
Expected: Subtotal=$45, Shipping=$5, Total=$50

Test 2: Add 10 items ($150 total) + free shipping threshold
Expected: Shipping=$0

Test 3: Add 11th item to cart
Expected: Error message "Maximum 10 items allowed"

Test 4: Apply invalid discount code "EXPIRED123"
Expected: Error message "Invalid discount code"

Test 5: Order value $0.50
Expected: Error message "Minimum order $1"
```

### Whitebox Testing Approach:

#### Pseudocode Analysis:
```java
public double calculateTotal(Cart cart, String discountCode) {
    if (cart.getItemCount() == 0) {           // Branch 1
        throw new Exception("Empty cart");
    }
    
    double subtotal = cart.getSubtotal();
    
    if (discountCode != null) {               // Branch 2
        if (isValidDiscount(discountCode)) {  // Branch 3
            subtotal = applyDiscount(subtotal, discountCode);
        } else {
            throw new Exception("Invalid discount");
        }
    }
    
    double shipping = 0;
    if (subtotal < 100) {                     // Branch 4
        shipping = cart.getShippingCost();
    }
    
    return subtotal + shipping;
}
```

#### Coverage Requirements:
- **Statement Coverage:** Execute all lines
- **Branch Coverage:** Test both true/false for each condition
- **Path Coverage:** Test all possible execution paths

#### Whitebox Test Cases:
```
Test W1: empty cart → covers branch 1 (exception path)
Test W2: discountCode = null → covers branch 2 (false)
Test W3: valid discount code → covers branch 2,3 (true,true)
Test W4: invalid discount code → covers branch 2,3 (true,false)
Test W5: subtotal < $100 → covers branch 4 (true)
Test W6: subtotal >= $100 → covers branch 4 (false)
```

---

## Example 2: User Authentication System

### Requirements:
- Username: 3-20 characters, alphanumeric only
- Password: 8-50 characters, must contain uppercase, lowercase, digit
- Account locks after 3 failed attempts
- Session expires after 30 minutes of inactivity

### Blackbox Testing Strategy:

#### Boundary Value Analysis:
| Parameter | Below Min | Min | Min+1 | Max-1 | Max | Above Max |
|-----------|-----------|-----|-------|-------|-----|-----------|
| Username | 2 chars | 3 chars | 4 chars | 19 chars | 20 chars | 21 chars |
| Password | 7 chars | 8 chars | 9 chars | 49 chars | 50 chars | 51 chars |

#### Error Guessing Test Cases:
```
Test 1: SQL injection attempt in username
Input: "admin'; DROP TABLE users; --"
Expected: Rejected as invalid characters

Test 2: XSS attempt in password  
Input: "<script>alert('hack')</script>"
Expected: Accepted if meets other criteria (special chars allowed)

Test 3: Unicode characters
Input: Username with café, naïve
Expected: Behavior based on requirements (accept/reject?)

Test 4: Empty fields
Input: "", ""
Expected: Clear error messages

Test 5: Very long inputs (buffer overflow test)
Input: 1000-character strings
Expected: Graceful truncation or rejection
```

### Whitebox Testing Strategy:

#### Code Structure Analysis:
```java
public AuthResult authenticate(String username, String password) {
    // Validation section
    if (!isValidUsername(username)) {         // Branch 1
        return AuthResult.INVALID_USERNAME;
    }
    
    if (!isValidPassword(password)) {         // Branch 2
        return AuthResult.INVALID_PASSWORD;
    }
    
    // Database lookup
    User user = findUser(username);
    if (user == null) {                       // Branch 3
        return AuthResult.USER_NOT_FOUND;
    }
    
    // Account status check
    if (user.isLocked()) {                    // Branch 4
        return AuthResult.ACCOUNT_LOCKED;
    }
    
    // Password verification
    if (verifyPassword(password, user.getHashedPassword())) { // Branch 5
        user.resetFailedAttempts();
        return AuthResult.SUCCESS;
    } else {
        user.incrementFailedAttempts();
        if (user.getFailedAttempts() >= 3) {  // Branch 6
            user.lockAccount();
        }
        return AuthResult.INVALID_CREDENTIALS;
    }
}
```

#### Path Coverage Tests:
```
Path 1: Invalid username format
Path 2: Invalid password format  
Path 3: Valid format, user not found
Path 4: Valid format, account locked
Path 5: Valid format, correct password
Path 6: Valid format, wrong password (1st/2nd attempt)
Path 7: Valid format, wrong password (3rd attempt - triggers lock)
```

---

## Example 3: Banking Transaction System

### Complex Business Logic:
```java
public class BankTransfer {
    public TransferResult transfer(Account from, Account to, double amount) {
        // Validation cluster
        if (amount <= 0) return INVALID_AMOUNT;
        if (from.equals(to)) return SAME_ACCOUNT;
        if (!from.isActive() || !to.isActive()) return INACTIVE_ACCOUNT;
        
        // Business rules cluster  
        double fee = calculateFee(amount, from.getType());
        double totalDebit = amount + fee;
        
        if (from.getBalance() < totalDebit) {
            if (from.hasOverdraftProtection() && 
                from.getOverdraftLimit() >= totalDebit - from.getBalance()) {
                // Overdraft allowed
            } else {
                return INSUFFICIENT_FUNDS;
            }
        }
        
        // Daily limits cluster
        double dailyTotal = from.getTodaysTransactionTotal() + amount;
        if (dailyTotal > from.getDailyLimit()) {
            return DAILY_LIMIT_EXCEEDED;
        }
        
        // Fraud detection cluster
        if (isHighRiskTransaction(from, to, amount)) {
            if (requiresApproval(amount)) {
                return PENDING_APPROVAL;
            }
        }
        
        // Execute transfer
        from.debit(totalDebit);
        to.credit(amount);
        logTransaction(from, to, amount, fee);
        
        return SUCCESS;
    }
}
```

### Testing Strategy Comparison:

#### Blackbox Approach:
- Focus on business rules and user scenarios
- Test regulatory compliance requirements
- Verify error messages and user experience
- Test integration with external fraud systems

#### Whitebox Approach:  
- Ensure all validation paths are tested
- Test complex conditional logic thoroughly
- Verify exception handling in all branches
- Test edge cases in calculation logic

#### Combined Approach Benefits:
- Blackbox ensures business value is tested
- Whitebox ensures code quality and robustness  
- Together they provide comprehensive coverage
- Each approach catches different types of defects

---

## Example 4: API Rate Limiting

### System Behavior:
```java
@RestController
public class APIController {
    
    @RateLimited(requests = 100, window = 3600) // 100 requests per hour
    @GetMapping("/api/data")
    public ResponseEntity<?> getData(@RequestParam String userId) {
        if (isBlacklisted(userId)) {
            return ResponseEntity.status(403).body("User blocked");
        }
        
        if (!isValidUser(userId)) {
            return ResponseEntity.badRequest().body("Invalid user");
        }
        
        try {
            Data data = dataService.fetchData(userId);
            if (data == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(data);
        } catch (ServiceException e) {
            return ResponseEntity.status(500).body("Service unavailable");
        }
    }
}
```

### Blackbox Testing Focus:
- API behavior under normal load
- Rate limiting functionality from client perspective
- Error response formats and HTTP status codes
- Authentication and authorization flows

### Whitebox Testing Focus:
- Rate limiting algorithm implementation
- Exception handling paths
- Database connection edge cases  
- Memory usage under high load

---

## Testing Tools and Automation

### Blackbox Testing Tools:
- **Postman/Newman:** API testing and automation
- **Selenium:** Web UI testing
- **JMeter:** Performance and load testing
- **REST Assured:** API testing in Java

### Whitebox Testing Tools:
- **JaCoCo:** Java code coverage
- **SonarQube:** Code quality and coverage
- **PITest:** Mutation testing
- **SpotBugs:** Static analysis

### Example JaCoCo Integration:
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.7</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Coverage Thresholds:
```xml
<rules>
    <rule>
        <element>BUNDLE</element>
        <limits>
            <limit>
                <counter>LINE</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.80</minimum>
            </limit>
            <limit>
                <counter>BRANCH</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.75</minimum>
            </limit>
        </limits>
    </rule>
</rules>
```

---

## Real-World Integration Examples

### Example: CI/CD Pipeline Integration

```yaml
# GitHub Actions example
name: Testing Pipeline
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      
      # Blackbox API tests
      - name: Run API Tests
        run: |
          docker-compose up -d
          sleep 30
          newman run postman-collection.json
          
      # Whitebox unit tests with coverage
      - name: Run Unit Tests
        run: |
          mvn test jacoco:report
          
      # Coverage enforcement
      - name: Check Coverage
        run: |
          mvn jacoco:check
          
      # Quality gate
      - name: SonarCloud Scan
        uses: SonarSource/sonarcloud-github-action@master
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
```

### Example: Test Reporting
```java
@Test
@DisplayName("Blackbox: Valid transfer within daily limit")
void testValidTransfer() {
    // Given
    Account source = createAccount("ACC001", 1000.0);
    Account target = createAccount("ACC002", 0.0);
    
    // When
    TransferResult result = transferService.transfer(source, target, 500.0);
    
    // Then
    assertEquals(TransferResult.SUCCESS, result);
    assertEquals(495.0, source.getBalance()); // Including $5 fee
    assertEquals(500.0, target.getBalance());
}

@Test
@DisplayName("Whitebox: Overdraft protection branch coverage")
void testOverdraftProtectionPath() {
    // Test specific code path for overdraft logic
    Account overdraftAccount = createAccountWithOverdraft("ACC001", 100.0, 200.0);
    
    TransferResult result = transferService.transfer(overdraftAccount, target, 250.0);
    
    assertEquals(TransferResult.SUCCESS, result);
    assertTrue(overdraftAccount.getBalance() < 0); // Verify overdraft used
}
```

This comprehensive set of examples demonstrates how blackbox and whitebox testing approaches complement each other in real-world scenarios, providing both functional validation and structural assurance.