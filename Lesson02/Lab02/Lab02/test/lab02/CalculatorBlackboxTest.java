package lab02;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Blackbox tests for Calculator class
 * These tests focus on functional behavior without examining internal implementation
 * Techniques used: Equivalence Partitioning, Boundary Value Analysis, Error Guessing
 */
@DisplayName("Calculator Blackbox Tests")
class CalculatorBlackboxTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    // ADDITION TESTS - Equivalence Partitioning
    @Test
    @DisplayName("Add two positive numbers")
    void testAddPositiveNumbers() {
        assertEquals(5.0, calculator.add(2, 3), 0.001);
        assertEquals(10.5, calculator.add(5.5, 5.0), 0.001);
    }

    @Test
    @DisplayName("Add positive and negative numbers")
    void testAddMixedNumbers() {
        assertEquals(2.0, calculator.add(5, -3), 0.001);
        assertEquals(-2.0, calculator.add(-5, 3), 0.001);
    }

    @Test
    @DisplayName("Add two negative numbers")
    void testAddNegativeNumbers() {
        assertEquals(-8.0, calculator.add(-5, -3), 0.001);
    }

    @Test
    @DisplayName("Add with zero")
    void testAddWithZero() {
        assertEquals(5.0, calculator.add(5, 0), 0.001);
        assertEquals(5.0, calculator.add(0, 5), 0.001);
        assertEquals(0.0, calculator.add(0, 0), 0.001);
    }

    // SUBTRACTION TESTS - Equivalence Partitioning
    @Test
    @DisplayName("Subtract positive numbers")
    void testSubtractPositiveNumbers() {
        assertEquals(2.0, calculator.subtract(5, 3), 0.001);
        assertEquals(-2.0, calculator.subtract(3, 5), 0.001);
    }

    @Test
    @DisplayName("Subtract with negative numbers")
    void testSubtractWithNegativeNumbers() {
        assertEquals(8.0, calculator.subtract(5, -3), 0.001);
        assertEquals(-8.0, calculator.subtract(-5, 3), 0.001);
    }

    // MULTIPLICATION TESTS - Equivalence Partitioning
    @Test
    @DisplayName("Multiply positive numbers")
    void testMultiplyPositiveNumbers() {
        assertEquals(15.0, calculator.multiply(3, 5), 0.001);
        assertEquals(12.25, calculator.multiply(3.5, 3.5), 0.001);
    }

    @Test
    @DisplayName("Multiply with zero")
    void testMultiplyWithZero() {
        assertEquals(0.0, calculator.multiply(5, 0), 0.001);
        assertEquals(0.0, calculator.multiply(0, 5), 0.001);
    }

    @Test
    @DisplayName("Multiply with negative numbers")
    void testMultiplyWithNegativeNumbers() {
        assertEquals(-15.0, calculator.multiply(3, -5), 0.001);
        assertEquals(-15.0, calculator.multiply(-3, 5), 0.001);
        assertEquals(15.0, calculator.multiply(-3, -5), 0.001);
    }

    // DIVISION TESTS - Boundary Value Analysis and Error Conditions
    @Test
    @DisplayName("Divide positive numbers")
    void testDividePositiveNumbers() {
        assertEquals(2.0, calculator.divide(6, 3), 0.001);
        assertEquals(2.5, calculator.divide(5, 2), 0.001);
    }

    @Test
    @DisplayName("Divide by zero throws exception")
    void testDivideByZero() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> calculator.divide(5, 0)
        );
        assertEquals("Division by zero is not allowed", exception.getMessage());
    }

    @Test
    @DisplayName("Divide zero by number")
    void testDivideZeroByNumber() {
        assertEquals(0.0, calculator.divide(0, 5), 0.001);
    }

    @Test
    @DisplayName("Divide negative numbers")
    void testDivideNegativeNumbers() {
        assertEquals(-2.0, calculator.divide(6, -3), 0.001);
        assertEquals(-2.0, calculator.divide(-6, 3), 0.001);
        assertEquals(2.0, calculator.divide(-6, -3), 0.001);
    }

    // POWER TESTS - Boundary Value Analysis
    @Test
    @DisplayName("Power with positive base and exponent")
    void testPowerPositive() {
        assertEquals(8.0, calculator.power(2, 3), 0.001);
        assertEquals(25.0, calculator.power(5, 2), 0.001);
    }

    @Test
    @DisplayName("Power with zero exponent")
    void testPowerZeroExponent() {
        assertEquals(1.0, calculator.power(5, 0), 0.001);
        assertEquals(1.0, calculator.power(-5, 0), 0.001);
    }

    @Test
    @DisplayName("Power with negative exponent")
    void testPowerNegativeExponent() {
        assertEquals(0.25, calculator.power(2, -2), 0.001);
        assertEquals(0.5, calculator.power(2, -1), 0.001);
    }

    @Test
    @DisplayName("Power with zero base and negative exponent throws exception")
    void testPowerZeroBaseNegativeExponent() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calculator.power(0, -1)
        );
        assertEquals("Cannot raise 0 to a negative power", exception.getMessage());
    }

    // SQUARE ROOT TESTS - Boundary Value Analysis
    @Test
    @DisplayName("Square root of positive numbers")
    void testSqrtPositive() {
        assertEquals(3.0, calculator.sqrt(9), 0.001);
        assertEquals(5.0, calculator.sqrt(25), 0.001);
        assertEquals(2.236, calculator.sqrt(5), 0.001);
    }

    @Test
    @DisplayName("Square root boundary values")
    void testSqrtBoundaryValues() {
        assertEquals(0.0, calculator.sqrt(0), 0.001);
        assertEquals(1.0, calculator.sqrt(1), 0.001);
    }

    @Test
    @DisplayName("Square root of negative number throws exception")
    void testSqrtNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calculator.sqrt(-1)
        );
        assertEquals("Cannot calculate square root of negative number", exception.getMessage());
    }

    // FACTORIAL TESTS - Boundary Value Analysis
    @Test
    @DisplayName("Factorial of small positive numbers")
    void testFactorialSmallPositive() {
        assertEquals(1, calculator.factorial(0));
        assertEquals(1, calculator.factorial(1));
        assertEquals(2, calculator.factorial(2));
        assertEquals(6, calculator.factorial(3));
        assertEquals(24, calculator.factorial(4));
        assertEquals(120, calculator.factorial(5));
    }

    @Test
    @DisplayName("Factorial boundary value - maximum allowed")
    void testFactorialMaximum() {
        assertEquals(2432902008176640000L, calculator.factorial(20));
    }

    @Test
    @DisplayName("Factorial of negative number throws exception")
    void testFactorialNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calculator.factorial(-1)
        );
        assertEquals("Factorial is not defined for negative numbers", exception.getMessage());
    }

    @Test
    @DisplayName("Factorial too large throws exception")
    void testFactorialTooLarge() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calculator.factorial(21)
        );
        assertEquals("Factorial too large for long type", exception.getMessage());
    }

    // PRIME NUMBER TESTS - Equivalence Partitioning
    @Test
    @DisplayName("Test known prime numbers")
    void testKnownPrimes() {
        assertTrue(calculator.isPrime(2));
        assertTrue(calculator.isPrime(3));
        assertTrue(calculator.isPrime(5));
        assertTrue(calculator.isPrime(7));
        assertTrue(calculator.isPrime(11));
        assertTrue(calculator.isPrime(13));
        assertTrue(calculator.isPrime(17));
        assertTrue(calculator.isPrime(97));
    }

    @Test
    @DisplayName("Test known composite numbers")
    void testKnownComposites() {
        assertFalse(calculator.isPrime(4));
        assertFalse(calculator.isPrime(6));
        assertFalse(calculator.isPrime(8));
        assertFalse(calculator.isPrime(9));
        assertFalse(calculator.isPrime(10));
        assertFalse(calculator.isPrime(15));
        assertFalse(calculator.isPrime(21));
        assertFalse(calculator.isPrime(100));
    }

    @Test
    @DisplayName("Prime test boundary values")
    void testPrimeBoundaryValues() {
        assertTrue(calculator.isPrime(2)); // smallest prime
        assertFalse(calculator.isPrime(4)); // smallest composite > 2
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, -1, 0, 1})
    @DisplayName("Prime test invalid inputs")
    void testPrimeInvalidInputs(int number) {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calculator.isPrime(number)
        );
        assertEquals("Prime check is only defined for numbers >= 2", exception.getMessage());
    }

    // UTILITY METHODS TESTS
    @Test
    @DisplayName("Last operation tracking")
    void testLastOperationTracking() {
        calculator.add(1, 2);
        assertEquals("add", calculator.getLastOperation());
        
        calculator.subtract(1, 2);
        assertEquals("subtract", calculator.getLastOperation());
        
        calculator.multiply(1, 2);
        assertEquals("multiply", calculator.getLastOperation());
        
        calculator.divide(6, 2);
        assertEquals("divide", calculator.getLastOperation());
        
        calculator.reset();
        assertEquals("none", calculator.getLastOperation());
    }

    @Test
    @DisplayName("Debug mode functionality")
    void testDebugMode() {
        assertFalse(calculator.isDebugMode());
        
        calculator.setDebugMode(true);
        assertTrue(calculator.isDebugMode());
        
        calculator.setDebugMode(false);
        assertFalse(calculator.isDebugMode());
    }
}