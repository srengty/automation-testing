package lab02;

/**
 * Main application class for Lab02 - Blackbox and Whitebox Testing
 * Demonstrates the Calculator class functionality
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Lab 02: Blackbox and Whitebox Testing");
        System.out.println("=====================================");
        
        // Create calculator instances
        Calculator calculator = new Calculator();
        Calculator debugCalculator = new Calculator(true);
        
        System.out.println("\nBasic Calculator Operations:");
        System.out.println("5 + 3 = " + calculator.add(5, 3));
        System.out.println("10 - 4 = " + calculator.subtract(10, 4));
        System.out.println("6 * 7 = " + calculator.multiply(6, 7));
        System.out.println("15 / 3 = " + calculator.divide(15, 3));
        
        System.out.println("\nAdvanced Operations:");
        System.out.println("2^8 = " + calculator.power(2, 8));
        System.out.println("√16 = " + calculator.sqrt(16));
        System.out.println("5! = " + calculator.factorial(5));
        System.out.println("Is 17 prime? " + calculator.isPrime(17));
        
        System.out.println("\nDebug Mode Calculator:");
        debugCalculator.add(10, 20);
        debugCalculator.multiply(3, 4);
        
        System.out.println("\nLast operation performed: " + calculator.getLastOperation());
        
        System.out.println("\nRun the test classes to see blackbox vs whitebox testing approaches!");
        System.out.println("- CalculatorBlackboxTest: Functional testing without code knowledge");
        System.out.println("- CalculatorWhiteboxTest: Structural testing with complete code coverage");
    }
}