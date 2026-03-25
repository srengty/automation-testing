package lab02;

/**
 * Calculator class demonstrating various arithmetic operations
 * Used for blackbox and whitebox testing examples
 */
public class Calculator {
    
    private boolean debugMode;
    private String lastOperation;
    
    public Calculator() {
        this.debugMode = false;
        this.lastOperation = "none";
    }
    
    public Calculator(boolean debugMode) {
        this.debugMode = debugMode;
        this.lastOperation = "none";
    }
    
    /**
     * Adds two numbers
     * @param a first number
     * @param b second number
     * @return sum of a and b
     */
    public double add(double a, double b) {
        lastOperation = "add";
        if (debugMode) {
            System.out.println("Adding: " + a + " + " + b);
        }
        return a + b;
    }
    
    /**
     * Subtracts two numbers
     * @param a first number
     * @param b second number
     * @return difference of a and b
     */
    public double subtract(double a, double b) {
        lastOperation = "subtract";
        if (debugMode) {
            System.out.println("Subtracting: " + a + " - " + b);
        }
        return a - b;
    }
    
    /**
     * Multiplies two numbers
     * @param a first number
     * @param b second number
     * @return product of a and b
     */
    public double multiply(double a, double b) {
        lastOperation = "multiply";
        if (debugMode) {
            System.out.println("Multiplying: " + a + " * " + b);
        }
        return a * b;
    }
    
    /**
     * Divides two numbers
     * @param a dividend
     * @param b divisor
     * @return quotient of a and b
     * @throws IllegalArgumentException if divisor is zero
     */
    public double divide(double a, double b) {
        lastOperation = "divide";
        if (debugMode) {
            System.out.println("Dividing: " + a + " / " + b);
        }
        
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed");
        }
        
        return a / b;
    }
    
    /**
     * Calculates power of a number
     * @param base the base number
     * @param exponent the exponent
     * @return base raised to the power of exponent
     * @throws IllegalArgumentException for invalid inputs
     */
    public double power(double base, int exponent) {
        lastOperation = "power";
        if (debugMode) {
            System.out.println("Power: " + base + "^" + exponent);
        }
        
        // Handle special cases
        if (exponent == 0) {
            return 1;
        }
        
        if (base == 0 && exponent < 0) {
            throw new IllegalArgumentException("Cannot raise 0 to a negative power");
        }
        
        if (exponent < 0) {
            return 1.0 / power(base, -exponent);
        }
        
        double result = 1;
        for (int i = 0; i < exponent; i++) {
            result *= base;
        }
        return result;
    }
    
    /**
     * Calculates square root of a number
     * @param number the number to find square root of
     * @return square root of the number
     * @throws IllegalArgumentException if number is negative
     */
    public double sqrt(double number) {
        lastOperation = "sqrt";
        if (debugMode) {
            System.out.println("Square root of: " + number);
        }
        
        if (number < 0) {
            throw new IllegalArgumentException("Cannot calculate square root of negative number");
        }
        
        if (number == 0 || number == 1) {
            return number;
        }
        
        // Simple implementation using Newton's method
        double guess = number;
        double previousGuess;
        
        do {
            previousGuess = guess;
            guess = (guess + number / guess) / 2;
        } while (Math.abs(guess - previousGuess) > 0.000001);
        
        return guess;
    }
    
    /**
     * Calculates factorial of a number
     * @param n the number to find factorial of
     * @return factorial of n
     * @throws IllegalArgumentException if n is negative or too large
     */
    public long factorial(int n) {
        lastOperation = "factorial";
        if (debugMode) {
            System.out.println("Factorial of: " + n);
        }
        
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }
        
        if (n > 20) {
            throw new IllegalArgumentException("Factorial too large for long type");
        }
        
        if (n == 0 || n == 1) {
            return 1;
        }
        
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    
    /**
     * Determines if a number is prime
     * @param number the number to check
     * @return true if the number is prime, false otherwise
     * @throws IllegalArgumentException if number is less than 2
     */
    public boolean isPrime(int number) {
        lastOperation = "isPrime";
        if (debugMode) {
            System.out.println("Checking if prime: " + number);
        }
        
        if (number < 2) {
            throw new IllegalArgumentException("Prime check is only defined for numbers >= 2");
        }
        
        if (number == 2) {
            return true;
        }
        
        if (number % 2 == 0) {
            return false;
        }
        
        for (int i = 3; i <= Math.sqrt(number); i += 2) {
            if (number % i == 0) {
                return false;
            }
        }
        
        return true;
    }
    
    // Utility methods
    public String getLastOperation() {
        return lastOperation;
    }
    
    public boolean isDebugMode() {
        return debugMode;
    }
    
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
    
    public void reset() {
        lastOperation = "none";
    }
}