# Assessment and Evaluation Materials
## Blackbox and Whitebox Testing Session

---

## Pre-Session Knowledge Assessment

### Instructions:
Complete this brief assessment to help us tailor the session to your experience level.

#### 1. Rate your current experience (1=Beginner, 5=Expert):

| Area | Rating | Comments |
|------|--------|----------|
| Writing test cases from requirements | 1 2 3 4 5 | |
| Understanding code coverage metrics | 1 2 3 4 5 | |
| Using automated testing tools | 1 2 3 4 5 | |
| Analyzing test effectiveness | 1 2 3 4 5 | |

#### 2. Which testing activities do you currently perform? (Check all that apply)
- [ ] Manual functional testing
- [ ] Automated unit testing
- [ ] Integration testing
- [ ] Performance testing
- [ ] Security testing
- [ ] User acceptance testing
- [ ] Code review for testability

#### 3. What testing challenges does your team face most often?
- [ ] Insufficient test coverage
- [ ] Tests taking too long to run
- [ ] Tests breaking frequently  
- [ ] Difficulty testing edge cases
- [ ] Unclear testing requirements
- [ ] Limited testing tools/environment
- [ ] Other: ________________

---

## Mid-Session Knowledge Checks

### Quick Poll 1: After Blackbox Section
**Question:** Which technique would be most effective for testing a password validation function?

A) Statement coverage analysis  
B) Equivalence partitioning  
C) Cyclomatic complexity measurement  
D) Path coverage testing  

**Follow-up:** "Why did you choose that answer?"

### Quick Poll 2: After Whitebox Section  
**Question:** You achieve 100% statement coverage but only 50% branch coverage. This means:

A) Your testing is complete  
B) You have untested decision paths  
C) Your code has no conditional logic  
D) You need more integration tests  

**Discussion prompt:** "What would you do to improve the branch coverage?"

### Quick Poll 3: Strategy Selection
**Scenario:** You're testing a new payment processing API for an e-commerce site. The code is complex with many conditional paths for different payment methods, currencies, and fraud checks.

**Question:** Which testing approach would you prioritize first and why?

A) Start with blackbox testing the API endpoints  
B) Start with whitebox testing the internal logic  
C) Use both approaches simultaneously  
D) Focus only on integration testing  

---

## Workshop Assessment Rubrics

### Workshop 1: Test Classification Rubric

#### Scoring (Per question):
- **3 points:** Correct classification with accurate reasoning
- **2 points:** Correct classification with basic reasoning  
- **1 point:** Correct classification with weak/no reasoning
- **0 points:** Incorrect classification

#### Sample Answers:
1. **Login testing:** Blackbox (testing functional requirements)
2. **Branch execution:** Whitebox (testing code structure)
3. **Age boundaries:** Blackbox (boundary value analysis)
4. **Method calls:** Whitebox (coverage metric)

#### Assessment Notes:
- Look for understanding of testing focus (functionality vs. structure)
- Accept reasonable justifications even if different from expected
- Note misconceptions for later clarification

### Workshop 2: Password Validator Rubric

#### Blackbox Section (25 points total):
- **Equivalence partitions identified (10 points):**
  - Valid passwords (2 pts)
  - Length violations (2 pts)
  - Missing character types (4 pts)
  - Special cases (2 pts)

- **Boundary values identified (10 points):**
  - Length boundaries (5 pts)
  - Character type edges (5 pts)

- **Test case quality (5 points):**
  - Specific, testable inputs
  - Clear expected results

#### Whitebox Section (15 points total):
- **Path identification (10 points):**
  - All validation paths identified (6 pts)
  - Correct test inputs for paths (4 pts)

- **Coverage understanding (5 points):**
  - Demonstrates understanding of branch coverage goals

### Workshop 3: Path Coverage Rubric

#### Path Analysis (20 points total):
- **Correct path count (8 points):**
  - 9-12 paths identified: full credit
  - 7-8 paths identified: 6 points
  - 5-6 paths identified: 4 points
  - <5 paths identified: 2 points

- **Test case design (12 points):**
  - Inputs correctly chosen for paths (8 pts)
  - Expected outputs identified (4 pts)

#### Sample Solution:
**Paths identified (10 total):**
1. score < 0 → "Invalid score"
2. score > 100 → "Invalid score"  
3. hasBonus=true, score+5>100, final≥90 → "A"
4. hasBonus=true, score+5≤100, final≥90 → "A"
5. hasBonus=false, score≥90 → "A"
6. hasBonus=any, final 80-89 → "B"
7. hasBonus=any, final 70-79 → "C"
8. hasBonus=any, final 60-69 → "D"
9. hasBonus=any, final <60 → "F"

---

## Post-Session Comprehensive Assessment

### Part A: Multiple Choice (10 questions, 2 points each)

#### 1. Equivalence partitioning is most useful for:
A) Measuring code coverage  
B) Reducing the number of test cases needed  
C) Finding performance bottlenecks  
D) Automating test execution  

**Answer:** B

#### 2. Which coverage metric is strongest (most comprehensive)?
A) Statement coverage  
B) Branch coverage  
C) Path coverage  
D) Function coverage  

**Answer:** C

#### 3. A function has 4 independent if-statements. Minimum tests for 100% branch coverage:
A) 4 tests  
B) 8 tests  
C) 16 tests  
D) 2^4 = 16 tests  

**Answer:** B (each if-statement needs true/false testing)

#### 4. Blackbox testing is also known as:
A) Structural testing  
B) Clear-box testing  
C) Behavioral testing  
D) White-box testing  

**Answer:** C

#### 5. Boundary value analysis focuses on testing:
A) Average values in input ranges  
B) Values at the edges of input domains  
C) Random values in input ranges  
D) Only invalid input values  

**Answer:** B

#### 6. Code coverage tools measure:
A) How much of the code is documented  
B) How much of the code is optimized  
C) How much of the code is exercised by tests  
D) How much of the code is commented  

**Answer:** C

#### 7. When is whitebox testing most valuable?
A) During user acceptance testing  
B) During unit testing  
C) During performance testing  
D) During beta testing  

**Answer:** B

#### 8. Gray-box testing combines:
A) Manual and automated testing  
B) Functional and non-functional testing  
C) Blackbox and whitebox approaches  
D) Static and dynamic testing  

**Answer:** C

#### 9. Decision tables are particularly useful for testing:
A) Simple input validation  
B) Complex business rules with multiple conditions  
C) Performance requirements  
D) User interface layout  

**Answer:** B

#### 10. 100% code coverage guarantees:
A) Bug-free code  
B) Complete functional testing  
C) All code paths are executed  
D) All code paths are executed, but not necessarily correctly tested  

**Answer:** D

### Part B: Scenario Analysis (30 points total)

#### Scenario 1: Login System (10 points)
**Requirements:**
- Username: 3-20 characters, letters and numbers only
- Password: 8-30 characters, must include uppercase, lowercase, number
- Account locks after 5 failed attempts
- Special admin account "root" has different rules

**Tasks:**
1. Design 3 blackbox test cases (5 points)
2. Identify what whitebox testing would add (5 points)

#### Scenario 2: Discount Calculator (10 points)
**Code snippet provided:**
```java
public double calculateDiscount(Customer customer, double orderAmount) {
    if (customer.isVIP()) {
        if (orderAmount > 1000) {
            return orderAmount * 0.15; // 15% VIP high value
        } else {
            return orderAmount * 0.10; // 10% VIP standard  
        }
    } else {
        if (orderAmount > 500) {
            return orderAmount * 0.05; // 5% regular high value
        } else if (orderAmount > 100) {
            return orderAmount * 0.02; // 2% regular medium
        }
    }
    return 0; // No discount
}
```

**Tasks:**
1. Identify minimum tests for 100% branch coverage (5 points)
2. Design specific test inputs and expected outputs (5 points)

#### Scenario 3: Integration Testing Strategy (10 points)
You're testing a microservices architecture where a user management service integrates with:
- Authentication service
- Email notification service  
- Audit logging service
- Database

**Task:** Explain how you would apply blackbox and whitebox testing approaches to this integration testing challenge. Include specific examples.

### Part C: Practical Application (10 points)

#### Code Review Exercise:
Given this test method, identify what testing approach it represents and suggest improvements:

```java
@Test
public void testUserRegistration() {
    User user = new User("john@email.com", "Password123!");
    boolean result = registrationService.register(user);
    assertTrue(result);
}
```

**Analysis should include:**
- Testing approach identification
- Weaknesses in current test  
- Specific improvements with examples
- Additional test cases needed

---

## Assessment Answer Keys and Rubrics

### Scenario 1 Answer Key:

#### Blackbox test cases:
1. **Valid login:** username="user123", password="Password1" → Success
2. **Boundary username:** username="ab" (too short) → Error message
3. **Password complexity:** username="user123", password="password" (no uppercase/number) → Error message

#### Whitebox additions:
- Test all validation code paths
- Test account locking logic after exactly 5 attempts
- Test special admin account logic branches
- Test edge cases in character validation code

### Scenario 2 Answer Key:

#### Minimum tests for 100% branch coverage (4 tests):
1. **VIP, high value:** customer.isVIP()=true, orderAmount=1500 → 225.0 (15%)
2. **VIP, standard:** customer.isVIP()=true, orderAmount=800 → 80.0 (10%)  
3. **Regular, high:** customer.isVIP()=false, orderAmount=600 → 30.0 (5%)
4. **Regular, medium:** customer.isVIP()=false, orderAmount=200 → 4.0 (2%)
5. **No discount:** customer.isVIP()=false, orderAmount=50 → 0.0

---

## Session Feedback Form

### Content Evaluation:
1. **Clarity of explanations:** Excellent | Good | Average | Poor
2. **Relevance to your work:** Extremely | Very | Somewhat | Not at all
3. **Practical applicability:** High | Medium | Low
4. **Pace of delivery:** Too fast | Just right | Too slow

### Workshop Activities:
1. **Workshop difficulty level:** Too easy | Just right | Too hard
2. **Time allocation:** Too much | Just right | Too little  
3. **Most valuable workshop:** 1 | 2 | 3 | 4
4. **Least valuable workshop:** 1 | 2 | 3 | 4

### Lab Exercises:
1. **Technical setup:** Smooth | Some issues | Major problems
2. **Instructions clarity:** Very clear | Clear | Confusing
3. **Learning value:** High | Medium | Low

### Overall Satisfaction:
1. **Meet learning objectives:** Completely | Mostly | Partially | Not at all
2. **Recommend to colleague:** Definitely | Probably | Maybe | No
3. **Overall rating:** Excellent | Good | Average | Poor

### Open Feedback:
**What was the most valuable part of this session?**

**What would you change or improve?**

**What additional topics would be helpful?**

**Any other comments or suggestions?**

---

## Instructor Assessment Notes

### Student Performance Indicators:

#### Strong Understanding:
- Correctly identifies testing approach for scenarios
- Asks clarifying questions about requirements vs. implementation
- Makes connections between coverage metrics and quality
- Suggests practical improvements to test strategies

#### Needs Support:
- Confuses testing approaches (blackbox/whitebox)
- Focuses only on happy path scenarios
- Struggles with identifying edge cases
- Doesn't understand coverage limitations

#### Common Misconceptions:
- "100% coverage means perfect testing"
- "Blackbox testing is always easier"
- "Whitebox testing is only for developers"
- "All testing can be automated"

### Follow-up Actions:
- **Individual support:** For participants struggling with core concepts
- **Advanced resources:** For participants ready for deeper topics
- **Practical coaching:** For participants wanting to apply immediately
- **Tool guidance:** For participants needing technical implementation help