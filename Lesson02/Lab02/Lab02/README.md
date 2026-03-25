## Lab 02: Blackbox and Whitebox Testing - Extended Session

This comprehensive lab demonstrates practical implementation of blackbox and whitebox testing strategies using multiple Java classes and real-world scenarios.

## Learning Objectives

- Understand the fundamental differences between blackbox and whitebox testing approaches
- Master key blackbox techniques: equivalence partitioning, boundary value analysis, decision tables
- Implement whitebox testing with statement, branch, and path coverage
- Apply both approaches to complex business logic scenarios
- Compare effectiveness and efficiency of different testing strategies
- Use industry-standard testing tools and frameworks

## Session Structure (3 Hours)

### Part 1: Foundations (30 minutes)
- Testing strategy overview and philosophy
- Interactive introduction to both approaches
- Real-world applicability discussion

### Part 2: Blackbox Testing Deep Dive (50 minutes)  
- **Theory:** Equivalence partitioning and boundary value analysis
- **Workshop 1:** Test classification exercise
- **Workshop 2:** Password validator design challenge
- **Advanced:** Decision tables and error guessing

### Part 3: Whitebox Testing Deep Dive (50 minutes)
- **Theory:** Code coverage metrics and analysis
- **Workshop 3:** Path coverage analysis exercise  
- **Advanced:** Cyclomatic complexity and testing tools
- **Practice:** Live coverage measurement

### Part 4: Hands-on Labs (50 minutes)
- **Lab 1:** Calculator testing comparison (20 minutes)
- **Lab 2:** BankAccount comprehensive testing (25 minutes)  
- **Assessment:** Knowledge check and discussion (5 minutes)

## Project Structure

### Core Lab Materials
```
Lab02/
├── src/lab02/
│   ├── Calculator.java              # Basic arithmetic operations
│   ├── bank/
│   │   └── BankAccount.java         # Complex business logic
│   └── App.java                     # Demonstration runner
├── test/lab02/
│   ├── CalculatorBlackboxTest.java  # Functional requirement tests
│   ├── CalculatorWhiteboxTest.java  # Code coverage focused tests
│   └── bank/
│       ├── BankAccountBlackboxTest.java  # Business rule validation
│       └── BankAccountWhiteboxTest.java  # Comprehensive path testing
└── Workshop-Materials/
    ├── Workshop-Activities.md       # Interactive exercises
    ├── Instructor-Guide.md         # Complete session plan
    ├── Practical-Examples.md       # Real-world case studies
    └── Assessment-Materials.md     # Evaluation tools
```

## Featured Testing Scenarios

### Scenario 1: Calculator Testing
**Focus:** Basic arithmetic operations with edge cases
- **Blackbox approach:** Input validation, mathematical correctness, error handling
- **Whitebox approach:** Branch coverage, exception paths, debug mode testing
- **Key learning:** Comparing test design strategies for simple logic

### Scenario 2: BankAccount Testing  
**Focus:** Complex financial business rules
- **Blackbox approach:** Business requirement validation, regulatory compliance
- **Whitebox approach:** Complex conditional logic, state management, path coverage
- **Key learning:** Testing sophisticated business logic comprehensively

## Workshop Activities

### Workshop 1: Test Classification (10 minutes)
Interactive exercise to distinguish blackbox vs whitebox test scenarios
- 10 realistic testing scenarios
- Pair/group discussion format
- Immediate feedback and clarification

### Workshop 2: Password Validator Design (15 minutes)
Hands-on test case design exercise
- **Requirements:** Complex validation rules for password strength
- **Deliverable:** Complete test suite using both approaches
- **Skills practiced:** Equivalence partitioning, boundary analysis, path identification

### Workshop 3: Path Coverage Analysis (10 minutes)
Code analysis and test design challenge
- **Given:** Complex grading function with multiple conditions
- **Task:** Identify all execution paths and design minimal test set
- **Skills practiced:** Control flow analysis, test optimization

### Workshop 4: E-commerce Testing Strategy (20 minutes)
Group project simulating real-world testing decisions
- **Teams:** 4 groups working on different features
- **Deliverable:** Testing approach recommendations with rationale
- **Skills practiced:** Strategy selection, test planning, presentation

## Technical Requirements

### Development Environment
- **Java:** JDK 11 or higher
- **IDE:** VS Code with Extension Pack for Java
- **Testing:** JUnit 5 (Jupiter)
- **Build:** Maven (optional) or VS Code Java project
- **Coverage:** JaCoCo integration available

### Dependencies
The lab uses standard Java libraries plus:
```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.8.2</version>
    <scope>test</scope>
</dependency>
```

## Running the Labs

### Option 1: VS Code (Recommended)
1. Open Lab02 folder in VS Code
2. Ensure Java Extension Pack is installed
3. Use Testing view to run individual test classes
4. Use "Run Java" to execute App.java demonstrations

### Option 2: Command Line
```bash
# Compile all source files
javac -cp "lib/*" src/lab02/*.java src/lab02/bank/*.java

# Compile test files  
javac -cp "lib/*:bin" test/lab02/*.java test/lab02/bank/*.java

# Run specific test class
java -cp "lib/*:bin" org.junit.platform.console.ConsoleLauncher --select-class lab02.CalculatorBlackboxTest

# Run all tests
java -cp "lib/*:bin" org.junit.platform.console.ConsoleLauncher --scan-classpath
```

## Learning Progression

### Beginner Level
- Start with Workshop 1 (classification)
- Focus on Calculator testing (simpler logic)
- Understand basic coverage concepts
- Practice writing clear test cases

### Intermediate Level  
- Complete all workshops
- Analyze both Calculator and BankAccount tests
- Compare testing approaches systematically
- Experiment with coverage tools

### Advanced Level
- Design additional test scenarios
- Optimize test suites for efficiency
- Explore edge cases and error conditions
- Integrate with CI/CD pipelines

## Key Learning Outcomes

After completing this lab, participants will be able to:

### Blackbox Testing Mastery
- ✅ Apply equivalence partitioning to partition input domains
- ✅ Use boundary value analysis for edge case identification
- ✅ Design decision tables for complex business rules
- ✅ Create comprehensive functional test suites

### Whitebox Testing Expertise  
- ✅ Understand and measure statement, branch, and path coverage
- ✅ Analyze code structure for test design
- ✅ Identify untested execution paths
- ✅ Use coverage tools effectively

### Strategic Testing Skills
- ✅ Choose appropriate testing approaches for different scenarios
- ✅ Balance testing effort with coverage goals
- ✅ Combine blackbox and whitebox strategies effectively
- ✅ Communicate testing rationale clearly

## Assessment and Evaluation

### Continuous Assessment
- **Workshop participation:** Interactive exercises throughout session
- **Lab completion:** Successful execution and analysis of test suites
- **Code review:** Understanding demonstrated through test analysis

### Final Evaluation
- **Knowledge check:** Core concept verification  
- **Practical application:** Problem-solving with testing strategies
- **Critical thinking:** Evaluation of testing approach trade-offs

## Extensions and Next Steps

### Immediate Follow-up
- Apply techniques to current project code
- Research coverage tools for your technology stack  
- Practice with additional complex scenarios

### Advanced Topics (Future Learning)
- Property-based testing and fuzzing
- Mutation testing for test quality assessment
- Test-driven development integration
- Performance testing strategies

### Professional Development
- ISTQB certification pathway
- Industry conference attendance  
- Open source testing tool contribution
- Team training and knowledge sharing

## Resources and References

### Essential Reading
- "Software Testing: A Comprehensive Approach" by Myers, Glenford J.
- "Effective Software Testing" by Dustin, Rashka & Paul
- ISTQB Foundation Level Syllabus

### Online Resources
- [Google Testing Blog](https://testing.googleblog.com/)
- [Martin Fowler on Testing](https://martinfowler.com/tags/testing.html)  
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

### Tools and Frameworks
- **Coverage:** JaCoCo, Cobertura, EclEmma
- **Mutation:** PITest for Java
- **Static Analysis:** SpotBugs, SonarQube
- **Test Generation:** JUnit QuickCheck, jqwik

## Support and Community

### Getting Help
- Workshop materials include detailed answer keys
- Instructor guide provides troubleshooting support
- Community forums and Stack Overflow for technical issues

### Contributing
- Submit improvements to lab exercises
- Share additional real-world examples
- Contribute to testing tool documentation

---

**Ready to master both blackbox and whitebox testing? Let's begin with the foundations and work our way up to expert-level testing strategies!**