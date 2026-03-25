package lab02;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Whitebox tests for Calculator class
 * These tests focus on code structure coverage and execution paths
 * Techniques used: Statement Coverage, Branch Coverage, Path Coverage
 */
@DisplayName("Calculator Whitebox Tests")
class CalculatorWhiteboxTest {

    private Calculator calculator;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
        // Setup for capturing console output when testing debug mode
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    // CONSTRUCTOR COVERAGE TESTS
    @Test
    @DisplayName("Default constructor path coverage")
    void testDefaultConstructor() {
        Calculator calc = new Calculator();
        assertFalse(calc.isDebugMode());
        assertEquals("none", calc.getLastOperation());
    }

    @Test
    @DisplayName("Parameterized constructor path coverage")
    void testParameterizedConstructor() {
        // Test both constructor paths
        Calculator calcDebugOff = new Calculator(false);
        assertFalse(calcDebugOff.isDebugMode());
        assertEquals("none", calcDebugOff.getLastOperation());
        
        Calculator calcDebugOn = new Calculator(true);
        assertTrue(calcDebugOn.isDebugMode());
        assertEquals("none", calcDebugOn.getLastOperation());
    }

    // ADD METHOD - STATEMENT AND BRANCH COVERAGE
    @Test
    @DisplayName("Add method - all execution paths")
    void testAddMethodPaths() {
        // Path 1: Debug mode off
        double result1 = calculator.add(2, 3);
        assertEquals(5.0, result1);
        assertEquals("add", calculator.getLastOperation());
        
        // Path 2: Debug mode on
        calculator.setDebugMode(true);
        double result2 = calculator.add(4, 5);
        assertEquals(9.0, result2);
        assertEquals("add", calculator.getLastOperation());
        assertTrue(outputStream.toString().contains("Adding: 4.0 + 5.0"));
    }

    // SUBTRACT METHOD - STATEMENT AND BRANCH COVERAGE
    @Test
    @DisplayName("Subtract method - all execution paths")
    void testSubtractMethodPaths() {
        // Path 1: Debug mode off
        double result1 = calculator.subtract(5, 3);
        assertEquals(2.0, result1);
        assertEquals("subtract", calculator.getLastOperation());
        
        // Path 2: Debug mode on
        calculator.setDebugMode(true);
        double result2 = calculator.subtract(10, 4);
        assertEquals(6.0, result2);
        assertTrue(outputStream.toString().contains("Subtracting: 10.0 - 4.0"));
    }

    // MULTIPLY METHOD - STATEMENT AND BRANCH COVERAGE
    @Test
    @DisplayName("Multiply method - all execution paths")
    void testMultiplyMethodPaths() {
        // Path 1: Debug mode off
        double result1 = calculator.multiply(3, 4);
        assertEquals(12.0, result1);
        assertEquals("multiply", calculator.getLastOperation());
        
        // Path 2: Debug mode on
        calculator.setDebugMode(true);
        double result2 = calculator.multiply(2, 6);
        assertEquals(12.0, result2);
        assertTrue(outputStream.toString().contains("Multiplying: 2.0 * 6.0"));
    }

    // DIVIDE METHOD - COMPLETE BRANCH COVERAGE
    @Test
    @DisplayName("Divide method - all branches covered")
    void testDivideMethodBranches() {
        // Branch 1: Debug mode off, normal division
        double result1 = calculator.divide(8, 2);
        assertEquals(4.0, result1);
        assertEquals("divide", calculator.getLastOperation());
        
        // Branch 2: Debug mode on, normal division
        calculator.setDebugMode(true);
        double result2 = calculator.divide(15, 3);
        assertEquals(5.0, result2);
        assertTrue(outputStream.toString().contains("Dividing: 15.0 / 3.0"));
        
        // Branch 3: Division by zero (exception path)
        assertThrows(IllegalArgumentException.class, () -> calculator.divide(5, 0));
    }

    // POWER METHOD - COMPLEX PATH COVERAGE
    @Test
    @DisplayName("Power method - complete path coverage")
    void testPowerMethodPaths() {
        calculator.setDebugMode(true);
        
        // Path 1: exponent == 0
        double result1 = calculator.power(5, 0);
        assertEquals(1.0, result1);
        assertTrue(outputStream.toString().contains("Power: 5.0^0"));
        
        // Clear output for next test
        outputStream.reset();
        
        // Path 2: base == 0 && exponent < 0 (exception)
        assertThrows(IllegalArgumentException.class, () -> calculator.power(0, -2));
        
        // Path 3: exponent < 0 (recursive call)
        double result3 = calculator.power(2, -2);
        assertEquals(0.25, result3, 0.001);
        
        // Clear output for next test
        outputStream.reset();
        
        // Path 4: positive exponent (loop execution)
        double result4 = calculator.power(3, 3);
        assertEquals(27.0, result4);
        assertTrue(outputStream.toString().contains("Power: 3.0^3"));
    }

    // SQRT METHOD - BRANCH COVERAGE
    @Test
    @DisplayName("Square root method - all branches")
    void testSqrtMethodBranches() {
        calculator.setDebugMode(true);
        
        // Branch 1: number < 0 (exception)
        assertThrows(IllegalArgumentException.class, () -> calculator.sqrt(-1));
        
        // Branch 2: number == 0
        double result2 = calculator.sqrt(0);
        assertEquals(0.0, result2);
        
        // Branch 3: number == 1
        double result3 = calculator.sqrt(1);
        assertEquals(1.0, result3);
        
        // Branch 4: Newton's method loop
        double result4 = calculator.sqrt(4);
        assertEquals(2.0, result4, 0.001);
        assertTrue(outputStream.toString().contains("Square root of: 4.0"));
    }

    // FACTORIAL METHOD - COMPLETE BRANCH COVERAGE
    @Test
    @DisplayName("Factorial method - all execution paths")
    void testFactorialMethodPaths() {
        calculator.setDebugMode(true);
        
        // Path 1: n < 0 (exception)
        assertThrows(IllegalArgumentException.class, () -> calculator.factorial(-1));
        
        // Path 2: n > 20 (exception)
        assertThrows(IllegalArgumentException.class, () -> calculator.factorial(21));
        
        // Path 3: n == 0 or n == 1 (early return)
        long result1 = calculator.factorial(0);
        assertEquals(1, result1);
        
        outputStream.reset();
        long result2 = calculator.factorial(1);
        assertEquals(1, result2);
        
        // Path 4: Loop execution for n > 1
        outputStream.reset();
        long result3 = calculator.factorial(5);
        assertEquals(120, result3);
        assertTrue(outputStream.toString().contains("Factorial of: 5"));
    }

    // ISPRIME METHOD - COMPLETE PATH COVERAGE
    @Test
    @DisplayName("isPrime method - all execution paths")
    void testIsPrimeMethodPaths() {
        calculator.setDebugMode(true);
        
        // Path 1: number < 2 (exception)
        assertThrows(IllegalArgumentException.class, () -> calculator.isPrime(1));
        
        // Path 2: number == 2 (return true)
        boolean result1 = calculator.isPrime(2);
        assertTrue(result1);
        assertTrue(outputStream.toString().contains("Checking if prime: 2"));
        
        // Path 3: number % 2 == 0 (even number, return false)
        outputStream.reset();
        boolean result2 = calculator.isPrime(4);
        assertFalse(result2);
        
        // Path 4: Loop finds divisor (return false)
        outputStream.reset();
        boolean result3 = calculator.isPrime(9); // 3 * 3
        assertFalse(result3);
        
        // Path 5: Loop completes without finding divisor (return true)
        outputStream.reset();
        boolean result4 = calculator.isPrime(7);
        assertTrue(result4);
        assertTrue(outputStream.toString().contains("Checking if prime: 7"));
    }

    // EDGE CASE COVERAGE FOR LOOPS
    @Test
    @DisplayName("Loop edge cases - zero iterations")
    void testLoopEdgeCases() {
        // Power method with exponent 0 (no loop iterations)
        assertEquals(1.0, calculator.power(999, 0));
        
        // Factorial with n = 1 (no loop iterations)
        assertEquals(1, calculator.factorial(1));
        
        // isPrime with number = 2 (no loop iterations)
        assertTrue(calculator.isPrime(2));
    }

    // STATE CHANGE COVERAGE
    @Test
    @DisplayName("Test state changes in all methods")
    void testStateChanges() {
        // Verify lastOperation changes in each method
        assertEquals("none", calculator.getLastOperation());
        
        calculator.add(1, 1);
        assertEquals("add", calculator.getLastOperation());
        
        calculator.subtract(1, 1);
        assertEquals("subtract", calculator.getLastOperation());
        
        calculator.multiply(1, 1);
        assertEquals("multiply", calculator.getLastOperation());
        
        calculator.divide(1, 1);
        assertEquals("divide", calculator.getLastOperation());
        
        calculator.power(1, 1);
        assertEquals("power", calculator.getLastOperation());
        
        calculator.sqrt(1);
        assertEquals("sqrt", calculator.getLastOperation());
        
        calculator.factorial(1);
        assertEquals("factorial", calculator.getLastOperation());
        
        calculator.isPrime(2);
        assertEquals("isPrime", calculator.getLastOperation());
        
        calculator.reset();
        assertEquals("none", calculator.getLastOperation());
    }

    // DEBUG MODE COVERAGE
    @Test
    @DisplayName("Debug mode coverage for all methods")
    void testDebugModeCoverage() {
        calculator.setDebugMode(true);
        
        // Test debug output for each method
        calculator.add(1, 2);
        assertTrue(outputStream.toString().contains("Adding"));
        
        outputStream.reset();
        calculator.subtract(1, 2);
        assertTrue(outputStream.toString().contains("Subtracting"));
        
        outputStream.reset();
        calculator.multiply(1, 2);
        assertTrue(outputStream.toString().contains("Multiplying"));
        
        outputStream.reset();
        calculator.divide(4, 2);
        assertTrue(outputStream.toString().contains("Dividing"));
        
        outputStream.reset();
        calculator.power(2, 2);
        assertTrue(outputStream.toString().contains("Power"));
        
        outputStream.reset();
        calculator.sqrt(4);
        assertTrue(outputStream.toString().contains("Square root"));
        
        outputStream.reset();
        calculator.factorial(3);
        assertTrue(outputStream.toString().contains("Factorial"));
        
        outputStream.reset();
        calculator.isPrime(3);
        assertTrue(outputStream.toString().contains("Checking if prime"));
    }

    // CONDITION COVERAGE
    @Test
    @DisplayName("Complex condition coverage")
    void testComplexConditions() {
        // Test power method condition: base == 0 && exponent < 0
        // We need both parts to be true for exception
        assertThrows(IllegalArgumentException.class, () -> calculator.power(0, -1));
        
        // Test conditions that make it false
        assertDoesNotThrow(() -> calculator.power(1, -1)); // base != 0
        assertDoesNotThrow(() -> calculator.power(0, 1));  // exponent >= 0
        assertDoesNotThrow(() -> calculator.power(0, 0));  // exponent == 0 (special case)
    }

    // LOOP BOUNDARY TESTING
    @Test
    @DisplayName("Loop boundary testing")
    void testLoopBoundaries() {
        // Test power method loop boundaries
        assertEquals(1.0, calculator.power(5, 0)); // 0 iterations
        assertEquals(5.0, calculator.power(5, 1)); // 1 iteration
        assertEquals(25.0, calculator.power(5, 2)); // 2 iterations
        
        // Test factorial loop boundaries
        assertEquals(1, calculator.factorial(0)); // no loop
        assertEquals(1, calculator.factorial(1)); // no loop
        assertEquals(2, calculator.factorial(2)); // 1 iteration
        assertEquals(6, calculator.factorial(3)); // 2 iterations
        
        // Test isPrime loop boundaries
        assertTrue(calculator.isPrime(2)); // no loop (special case)
        assertTrue(calculator.isPrime(3)); // loop runs once
        assertFalse(calculator.isPrime(4)); // early exit (even)
        assertTrue(calculator.isPrime(5)); // loop runs once
    }
}