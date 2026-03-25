package lab02.bank;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Blackbox tests for BankAccount class
 * Tests focus on functional requirements without examining internal implementation
 */
@DisplayName("BankAccount Blackbox Tests")
class BankAccountBlackboxTest {

    private BankAccount account;
    private BankAccount targetAccount;

    @BeforeEach
    void setUp() {
        account = new BankAccount("ACC001", "John Doe", BankAccount.AccountType.CHECKING);
        targetAccount = new BankAccount("ACC002", "Jane Smith", BankAccount.AccountType.SAVINGS);
    }

    // CONSTRUCTOR TESTS - Input validation
    @Test
    @DisplayName("Valid account creation")
    void testValidAccountCreation() {
        BankAccount newAccount = new BankAccount("ACC123", "Alice Johnson", BankAccount.AccountType.SAVINGS);
        assertEquals("ACC123", newAccount.getAccountNumber());
        assertEquals("Alice Johnson", newAccount.getAccountHolderName());
        assertEquals(BankAccount.AccountType.SAVINGS, newAccount.getAccountType());
        assertEquals(0.0, newAccount.getBalance());
        assertTrue(newAccount.isActive());
        assertEquals(0, newAccount.getTransactionCount());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("Invalid account numbers")
    void testInvalidAccountNumbers(String invalidAccountNumber) {
        assertThrows(IllegalArgumentException.class, 
            () -> new BankAccount(invalidAccountNumber, "John Doe", BankAccount.AccountType.CHECKING));
    }

    @Test
    @DisplayName("Null inputs throw exceptions")
    void testNullInputs() {
        assertThrows(IllegalArgumentException.class, 
            () -> new BankAccount(null, "John Doe", BankAccount.AccountType.CHECKING));
        assertThrows(IllegalArgumentException.class, 
            () -> new BankAccount("ACC001", null, BankAccount.AccountType.CHECKING));
        assertThrows(IllegalArgumentException.class, 
            () -> new BankAccount("ACC001", "John Doe", null));
    }

    // DEPOSIT TESTS - Equivalence Partitioning
    @Test
    @DisplayName("Valid deposit amounts")
    void testValidDeposits() {
        account.deposit(100.0);
        assertEquals(100.0, account.getBalance());
        assertEquals(1, account.getTransactionCount());
        
        account.deposit(50.5);
        assertEquals(150.5, account.getBalance());
        assertEquals(2, account.getTransactionCount());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-100.0, 0.0, -0.1})
    @DisplayName("Invalid deposit amounts")
    void testInvalidDepositAmounts(double invalidAmount) {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(invalidAmount));
    }

    @Test
    @DisplayName("Large deposits for regular accounts")
    void testLargeDepositsRegularAccount() {
        // Regular checking account should reject large deposits
        assertThrows(IllegalArgumentException.class, () -> account.deposit(15000.0));
    }

    @ParameterizedTest
    @EnumSource(value = BankAccount.AccountType.class, names = {"BUSINESS", "VIP"})
    @DisplayName("Large deposits for business/VIP accounts")
    void testLargeDepositsBusinessVIPAccounts(BankAccount.AccountType accountType) {
        BankAccount businessAccount = new BankAccount("BUS001", "Company", accountType);
        assertDoesNotThrow(() -> businessAccount.deposit(15000.0));
        assertEquals(15000.0, businessAccount.getBalance());
    }

    @Test
    @DisplayName("Inactive account deposit")
    void testDepositInactiveAccount() {
        account.setActive(false);
        assertThrows(IllegalStateException.class, () -> account.deposit(100.0));
    }

    // WITHDRAWAL TESTS - Boundary Value Analysis
    @Test
    @DisplayName("Valid withdrawals within balance")
    void testValidWithdrawals() {
        account.deposit(500.0);
        account.withdraw(200.0);
        assertEquals(300.0, account.getBalance());
    }

    @Test
    @DisplayName("Withdrawal exactly equal to balance")
    void testWithdrawalEqualToBalance() {
        account.deposit(100.0);
        account.withdraw(100.0);
        assertEquals(0.0, account.getBalance());
    }

    @Test
    @DisplayName("Overdraft for checking account")
    void testOverdraftChecking() {
        // Checking account should allow overdraft up to limit
        account.deposit(100.0);
        assertDoesNotThrow(() -> account.withdraw(400.0)); // $300 overdraft
        assertEquals(-300.0, account.getBalance());
    }

    @Test
    @DisplayName("Excessive overdraft rejection")
    void testExcessiveOverdraft() {
        account.deposit(100.0);
        // Should reject withdrawal that exceeds overdraft limit
        assertThrows(IllegalStateException.class, () -> account.withdraw(700.0)); // Would be $600 overdraft
    }

    @Test
    @DisplayName("Business account withdrawal limits")
    void testBusinessAccountWithdrawalLimit() {
        BankAccount businessAccount = new BankAccount("BUS001", "Company", BankAccount.AccountType.BUSINESS);
        businessAccount.deposit(10000.0);
        
        // Should allow up to $5000
        assertDoesNotThrow(() -> businessAccount.withdraw(5000.0));
        
        // Should reject over $5000
        assertThrows(IllegalArgumentException.class, () -> businessAccount.withdraw(5001.0));
    }

    // TRANSFER TESTS - Error conditions and business rules
    @Test
    @DisplayName("Valid transfer")
    void testValidTransfer() {
        account.deposit(1000.0);
        account.transfer(targetAccount, 200.0);
        
        // Should deduct from source (including potential fee)
        assertTrue(account.getBalance() < 1000.0);
        // Should credit to target
        assertEquals(200.0, targetAccount.getBalance());
    }

    @Test
    @DisplayName("Transfer fee calculation for large amounts")
    void testTransferFeeCalculation() {
        account.deposit(1000.0);
        double initialBalance = account.getBalance();
        
        account.transfer(targetAccount, 500.0);
        
        // Should apply 1% fee for non-VIP accounts on amounts > $100
        double expectedFee = 500.0 * 0.01; // $5
        assertEquals(initialBalance - 500.0 - expectedFee, account.getBalance(), 0.01);
        assertEquals(500.0, targetAccount.getBalance());
    }

    @Test
    @DisplayName("Transfer to null account")
    void testTransferToNullAccount() {
        account.deposit(100.0);
        assertThrows(IllegalArgumentException.class, () -> account.transfer(null, 50.0));
    }

    @Test
    @DisplayName("Transfer to same account")
    void testTransferToSameAccount() {
        account.deposit(100.0);
        assertThrows(IllegalArgumentException.class, () -> account.transfer(account, 50.0));
    }

    @Test
    @DisplayName("Transfer to inactive account")
    void testTransferToInactiveAccount() {
        account.deposit(100.0);
        targetAccount.setActive(false);
        assertThrows(IllegalStateException.class, () -> account.transfer(targetAccount, 50.0));
    }

    // INTEREST CALCULATION TESTS
    @Test
    @DisplayName("Savings account interest calculation")
    void testSavingsInterestCalculation() {
        BankAccount savingsAccount = new BankAccount("SAV001", "Saver", BankAccount.AccountType.SAVINGS);
        
        // Low balance - lower rate
        savingsAccount.deposit(500.0);
        double lowBalanceInterest = savingsAccount.calculateInterest();
        
        // High balance - higher rate  
        savingsAccount.deposit(1000.0); // Total $1500
        double highBalanceInterest = savingsAccount.calculateInterest();
        
        assertTrue(highBalanceInterest > lowBalanceInterest);
    }

    @Test
    @DisplayName("Interest calculation for different account types")
    void testInterestByAccountType() {
        double amount = 1000.0;
        
        BankAccount[] accounts = {
            new BankAccount("CHK", "User1", BankAccount.AccountType.CHECKING),
            new BankAccount("SAV", "User2", BankAccount.AccountType.SAVINGS),
            new BankAccount("BUS", "User3", BankAccount.AccountType.BUSINESS),
            new BankAccount("VIP", "User4", BankAccount.AccountType.VIP)
        };
        
        for (BankAccount acc : accounts) {
            acc.deposit(amount);
        }
        
        // VIP should have highest interest rate
        assertTrue(accounts[3].calculateInterest() > accounts[0].calculateInterest());
        assertTrue(accounts[3].calculateInterest() > accounts[1].calculateInterest());
    }

    // MONTHLY FEE TESTS
    @Test
    @DisplayName("Monthly fee based on balance")
    void testMonthlyFeeCalculation() {
        // Low balance savings account
        BankAccount lowBalanceSavings = new BankAccount("LOW", "User", BankAccount.AccountType.SAVINGS);
        lowBalanceSavings.deposit(100.0); // Below $500 minimum
        
        double initialBalance = lowBalanceSavings.getBalance();
        lowBalanceSavings.applyMonthlyFee();
        assertTrue(lowBalanceSavings.getBalance() < initialBalance);
    }

    @Test
    @DisplayName("Account closure due to insufficient funds for fees")
    void testAccountClosureInsufficientFunds() {
        BankAccount lowBalanceAccount = new BankAccount("LOW", "User", BankAccount.AccountType.BUSINESS);
        lowBalanceAccount.deposit(10.0); // Insufficient for $25 business fee
        
        lowBalanceAccount.applyMonthlyFee();
        assertFalse(lowBalanceAccount.isActive());
    }

    // AUTO-UPGRADE TESTS
    @Test
    @DisplayName("Auto-upgrade to VIP account")
    void testAutoUpgradeToVIP() {
        BankAccount savingsAccount = new BankAccount("SAV", "User", BankAccount.AccountType.SAVINGS);
        savingsAccount.deposit(15000.0); // Above VIP threshold
        
        assertEquals(BankAccount.AccountType.VIP, savingsAccount.getAccountType());
    }

    // ACCOUNT STATEMENT TEST
    @Test
    @DisplayName("Account statement contains required information")
    void testAccountStatement() {
        account.deposit(500.0);
        account.withdraw(100.0);
        
        String statement = account.getAccountStatement();
        
        assertTrue(statement.contains("ACC001"));
        assertTrue(statement.contains("John Doe"));
        assertTrue(statement.contains("CHECKING"));
        assertTrue(statement.contains("400.00"));
        assertTrue(statement.contains("Active"));
        assertTrue(statement.contains("2")); // Transaction count
    }

    // EDGE CASES AND BOUNDARY VALUES
    @Test
    @DisplayName("Very large deposit amounts")
    void testVeryLargeAmounts() {
        BankAccount vipAccount = new BankAccount("VIP", "Rich User", BankAccount.AccountType.VIP);
        assertDoesNotThrow(() -> vipAccount.deposit(1000000.0));
        assertEquals(1000000.0, vipAccount.getBalance());
    }

    @Test
    @DisplayName("Floating point precision")
    void testFloatingPointPrecision() {
        account.deposit(0.01);
        account.deposit(0.02);
        assertEquals(0.03, account.getBalance(), 0.001);
    }
}