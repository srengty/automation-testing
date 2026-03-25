# Workshop Activities for Blackbox and Whitebox Testing

## Workshop 1: Test Classification Exercise (10 minutes)

### Instructions for Instructor:
1. Display the scenarios on screen or distribute handouts
2. Have students work in pairs for 5 minutes
3. Discuss answers as a group for 5 minutes

### Student Exercise:
**Classify each testing scenario as Blackbox (B) or Whitebox (W):**

1. Testing if a login system accepts valid username/password combinations
2. Verifying that all if-else branches in a sorting algorithm are executed
3. Testing age input field with values: -1, 0, 17, 18, 65, 66, 150
4. Ensuring every method in a class gets called during testing
5. Testing that an ATM rejects invalid PIN numbers
6. Checking that both true and false paths of a boolean condition are tested
7. Testing system response when database connection fails
8. Measuring statement coverage of unit tests
9. Testing shopping cart with 0, 1, and multiple items
10. Analyzing cyclomatic complexity of a function

### Answer Key:
1. B - Functional requirement testing
2. W - Code structure analysis
3. B - Boundary value analysis
4. W - Code coverage metric
5. B - Security requirement testing
6. W - Branch coverage testing
7. B - Error condition testing
8. W - Code coverage measurement
9. B - Equivalence partitioning
10. W - Code complexity analysis

### Discussion Points:
- Why is each classification correct?
- Can some scenarios use both approaches?
- What additional tests would you add?

---

## Workshop 2: Password Validator Design (15 minutes)

### Scenario:
Create test cases for a password strength validator with these requirements:
- Length: 8-20 characters
- Must contain: at least one uppercase letter
- Must contain: at least one lowercase letter  
- Must contain: at least one digit
- Must contain: at least one special character (!@#$%^&*)
- Cannot contain spaces
- Cannot be a common password (password123, admin, etc.)

### Student Tasks:

#### Part A: Blackbox Approach (8 minutes)
**Identify Equivalence Partitions:**

1. **Valid Passwords:** _______________________
2. **Too Short:** _______________________
3. **Too Long:** _______________________
4. **Missing Uppercase:** _______________________
5. **Missing Lowercase:** _______________________
6. **Missing Digit:** _______________________
7. **Missing Special Character:** _______________________
8. **Contains Spaces:** _______________________
9. **Common Password:** _______________________

**Design Boundary Value Tests:**
- Minimum length boundary: _______________________
- Maximum length boundary: _______________________
- Character type boundaries: _______________________

#### Part B: Implementation Analysis (7 minutes)
Given this pseudocode:
```
function validatePassword(password):
    if length(password) < 8 OR length(password) > 20:
        return "Invalid length"
    if not containsUppercase(password):
        return "Missing uppercase"
    if not containsLowercase(password):
        return "Missing lowercase"
    if not containsDigit(password):
        return "Missing digit"
    if not containsSpecial(password):
        return "Missing special character"
    if containsSpaces(password):
        return "No spaces allowed"
    if isCommonPassword(password):
        return "Password too common"
    return "Valid password"
```

**Whitebox Test Cases for 100% Branch Coverage:**

1. Path 1 (too short): _______________________
2. Path 2 (too long): _______________________
3. Path 3 (missing uppercase): _______________________
4. Path 4 (missing lowercase): _______________________
5. Path 5 (missing digit): _______________________
6. Path 6 (missing special): _______________________
7. Path 7 (contains spaces): _______________________
8. Path 8 (common password): _______________________
9. Path 9 (valid password): _______________________

---

## Workshop 3: Path Coverage Analysis (10 minutes)

### Code to Analyze:
```java
public String calculateGrade(int score, boolean hasBonus) {
    if (score < 0 || score > 100) {
        return "Invalid score";
    }
    
    if (hasBonus) {
        score += 5; // Bonus points
        if (score > 100) {
            score = 100; // Cap at 100
        }
    }
    
    if (score >= 90) {
        return "A";
    } else if (score >= 80) {
        return "B";  
    } else if (score >= 70) {
        return "C";
    } else if (score >= 60) {
        return "D";
    } else {
        return "F";
    }
}
```

### Student Tasks:

**1. Identify all possible execution paths (5 minutes):**

Path 1: \_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_

Path 2: \_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_

Path 3: \_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_

(Continue for all paths...)

**2. Design test cases for 100% path coverage (5 minutes):**

| Test Case | Score | HasBonus | Expected Result | Path Tested |
|-----------|-------|----------|-----------------|-------------|
| 1         |       |          |                 |             |
| 2         |       |          |                 |             |
| 3         |       |          |                 |             |
| ...       |       |          |                 |             |

### Answer Key Available for Instructor

---

## Workshop 4: Real-world Testing Scenarios (20 minutes)

### Scenario: E-commerce Shopping Cart

You're testing an online shopping cart system with these features:
- Add/remove items
- Apply discount codes  
- Calculate shipping costs
- Process payments
- Handle inventory limitations

### Group Activity Instructions:

**Divide class into 4 groups:**
- **Group 1:** Blackbox testing for Add/Remove items
- **Group 2:** Blackbox testing for Discount codes  
- **Group 3:** Whitebox testing for Shipping calculation
- **Group 4:** Whitebox testing for Payment processing

### Group Tasks (15 minutes):

**Each group creates:**
1. **Test scenarios** (what to test)
2. **Test data** (specific inputs)
3. **Expected results** 
4. **Testing rationale** (why this approach)

**Blackbox groups focus on:**
- User requirements and specifications
- Input/output behavior
- Error conditions
- Business rules

**Whitebox groups focus on:**
- Code coverage goals
- Execution paths
- Internal logic verification
- Edge cases in implementation

### Presentation (5 minutes):
Each group presents their approach and test cases

### Sample Framework for Groups:

#### Group Template:
**Feature:** \_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_
**Testing Approach:** Blackbox / Whitebox

**Test Cases:**

| ID | Scenario | Input Data | Expected Result | Notes |
|----|----------|------------|-----------------|-------|
| 1  |          |            |                 |       |
| 2  |          |            |                 |       |
| 3  |          |            |                 |       |

**Coverage Goals:**
- What are you trying to achieve?
- How will you measure success?

**Challenges:**
- What difficulties did you encounter?
- What assumptions did you make?

---

## Quick Assessment Quiz (5 minutes)

### Multiple Choice Questions:

**1. Which technique is primarily used in blackbox testing?**
   a) Statement coverage analysis
   b) Equivalence partitioning
   c) Path coverage measurement
   d) Cyclomatic complexity

**2. 100% branch coverage guarantees:**
   a) 100% statement coverage
   b) 100% path coverage  
   c) Error-free code
   d) Complete functional testing

**3. When is whitebox testing most valuable?**
   a) User acceptance testing
   b) Unit testing
   c) System integration testing
   d) Beta testing

**4. A function has 3 if-statements. Minimum tests for 100% branch coverage:**
   a) 3 tests
   b) 6 tests
   c) 8 tests
   d) Depends on nesting

**5. Equivalence partitioning is used to:**
   a) Measure code coverage
   b) Group similar test inputs
   c) Find memory leaks
   d) Test all execution paths

### Short Answer Questions:

**6. Explain the difference between blackbox and whitebox testing in one sentence each.**

Blackbox: \_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_

Whitebox: \_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_

**7. How would you test a login function using both approaches?**

Blackbox approach: \_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_

Whitebox approach: \_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_

### Answer Key:
1. b) Equivalence partitioning
2. a) 100% statement coverage  
3. b) Unit testing
4. c) 8 tests (2^3 combinations)
5. b) Group similar test inputs
6. Blackbox: Tests functionality without examining code / Whitebox: Tests code structure and internal logic
7. Blackbox: Test with valid/invalid credentials, edge cases / Whitebox: Ensure all branches and error paths are executed

---

## Extended Lab Instructions

### Lab Session Structure (40 minutes total):

#### Phase 1: Calculator Analysis (15 minutes)
1. **Examine existing code** (5 minutes)
   - Open Calculator.java
   - Review methods and logic
   - Identify decision points

2. **Run test suites** (5 minutes)
   - Execute CalculatorBlackboxTest
   - Execute CalculatorWhiteboxTest  
   - Compare results

3. **Coverage analysis** (5 minutes)
   - Identify missed test cases
   - Plan additional tests

#### Phase 2: BankAccount Testing (20 minutes)
1. **Requirements review** (5 minutes)
   - Read BankAccount functionality
   - Understand business rules
   - Note validation requirements

2. **Blackbox test design** (7 minutes)
   - Design test cases from requirements
   - Focus on input/output behavior
   - Include error scenarios

3. **Whitebox test analysis** (8 minutes)
   - Examine code structure
   - Identify all execution paths
   - Design comprehensive coverage tests

#### Phase 3: Comparison and Discussion (5 minutes)
1. **Compare approaches**
   - Which found more defects?
   - Which was easier to design?
   - Which provides better coverage?

2. **Real-world application**
   - When to use each approach
   - How to combine both effectively