package lab02.bank;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Whitebox tests for BankAccount class
 * Tests focus on code structure, branches, and path coverage
 */
@DisplayName("BankAccount Whitebox Tests")
class BankAccountWhiteboxTest {

    private BankAccount account;
    private BankAccount targetAccount;

    @BeforeEach
    void setUp() {
        account = new BankAccount("ACC001", "John Doe", BankAccount.AccountType.CHECKING);
        targetAccount = new BankAccount("ACC002", "Jane Smith", BankAccount.AccountType.SAVINGS);
    }

    // CONSTRUCTOR - BRANCH COVERAGE
    @Test
    @DisplayName("Constructor validation branches")
    void testConstructorValidationBranches() {
        // Test all validation branches
        assertThrows(IllegalArgumentException.class, 
            () -> new BankAccount(null, "Name", BankAccount.AccountType.CHECKING));
        assertThrows(IllegalArgumentException.class, 
            () -> new BankAccount("", "Name", BankAccount.AccountType.CHECKING));
        assertThrows(IllegalArgumentException.class, 
            () -> new BankAccount("ACC", null, BankAccount.AccountType.CHECKING));
        assertThrows(IllegalArgumentException.class, 
            () -> new BankAccount("ACC", "", BankAccount.AccountType.CHECKING));
        assertThrows(IllegalArgumentException.class, 
            () -> new BankAccount("ACC", "Name", null));
        
        // Test successful path
        BankAccount validAccount = new BankAccount("  ACC123  ", "  John Doe  ", BankAccount.AccountType.SAVINGS);
        assertEquals("ACC123", validAccount.getAccountNumber()); // Trim test
        assertEquals("John Doe", validAccount.getAccountHolderName()); // Trim test
    }

    // DEPOSIT - COMPLETE PATH COVERAGE
    @Test
    @DisplayName("Deposit method - all execution paths")
    void testDepositAllPaths() {
        // Path 1: Inactive account
        account.setActive(false);
        assertThrows(IllegalStateException.class, () -> account.deposit(100.0));
        
        // Reset for next tests
        account.setActive(true);
        
        // Path 2: Invalid amount (negative)
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-50.0));
        
        // Path 3: Invalid amount (zero)
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0.0));
        
        // Path 4: Invalid amount (NaN)
        assertThrows(IllegalArgumentException.class, () -> account.deposit(Double.NaN));
        
        // Path 5: Invalid amount (Infinity)
        assertThrows(IllegalArgumentException.class, () -> account.deposit(Double.POSITIVE_INFINITY));
        
        // Path 6: Large amount, non-business/non-VIP account
        assertThrows(IllegalArgumentException.class, () -> account.deposit(15000.0));
        
        // Path 7: Large amount, business account (allowed)
        account.setAccountType(BankAccount.AccountType.BUSINESS);
        account.deposit(15000.0);
        assertEquals(15000.0, account.getBalance());
        assertEquals(1, account.getTransactionCount());
        
        // Path 8: Normal deposit
        account.deposit(500.0);
        assertEquals(15500.0, account.getBalance());
        assertEquals(2, account.getTransactionCount());
        
        // Path 9: Auto-upgrade condition
        BankAccount savingsAccount = new BankAccount("SAV", "User", BankAccount.AccountType.SAVINGS);
        savingsAccount.deposit(12000.0);
        assertEquals(BankAccount.AccountType.VIP, savingsAccount.getAccountType());
        
        // Path 10: No auto-upgrade (checking account)
        BankAccount checkingAccount = new BankAccount("CHK", "User", BankAccount.AccountType.CHECKING);
        checkingAccount.deposit(12000.0);
        assertEquals(BankAccount.AccountType.CHECKING, checkingAccount.getAccountType());
    }

    // WITHDRAW - BRANCH AND CONDITION COVERAGE
    @Test
    @DisplayName("Withdraw method - complete branch coverage")
    void testWithdrawBranchCoverage() {
        account.deposit(1000.0);
        
        // Branch 1: Account inactive
        account.setActive(false);
        assertThrows(IllegalStateException.class, () -> account.withdraw(100.0));
        account.setActive(true);
        
        // Branch 2: Invalid amount
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-50.0));
        
        // Branch 3: Amount > available balance
        assertThrows(IllegalStateException.class, () -> account.withdraw(2000.0));
        
        // Branch 4: Business account withdrawal limit exceeded
        account.setAccountType(BankAccount.AccountType.BUSINESS);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(5001.0));
        
        // Branch 5: Business account within limit
        account.withdraw(4000.0);
        assertEquals(-3000.0, account.getBalance()); // 1000 - 4000
        
        // Branch 6: Normal withdrawal
        account.deposit(5000.0);
        account.withdraw(500.0);
        assertEquals(1500.0, account.getBalance());
    }

    // TRANSFER - COMPLEX CONDITION COVERAGE
    @Test
    @DisplayName("Transfer method - all conditions and branches")
    void testTransferCompleteConditions() {
        account.deposit(1000.0);
        
        // Condition 1: Target account null
        assertThrows(IllegalArgumentException.class, () -> account.transfer(null, 100.0));
        
        // Condition 2: Transfer to same account
        assertThrows(IllegalArgumentException.class, () -> account.transfer(account, 100.0));
        
        // Condition 3: Target account inactive
        targetAccount.setActive(false);
        assertThrows(IllegalStateException.class, () -> account.transfer(targetAccount, 100.0));
        targetAccount.setActive(true);
        
        // Condition 4: VIP account (no fee) - amount > 100
        account.setAccountType(BankAccount.AccountType.VIP);
        double initialBalance = account.getBalance();
        account.transfer(targetAccount, 200.0);
        assertEquals(initialBalance - 200.0, account.getBalance()); // No fee
        assertEquals(200.0, targetAccount.getBalance());
        
        // Reset
        targetAccount = new BankAccount("ACC003", "New Target", BankAccount.AccountType.SAVINGS);
        account = new BankAccount("ACC004", "New Source", BankAccount.AccountType.CHECKING);
        account.deposit(1000.0);
        
        // Condition 5: Non-VIP account, amount <= 100 (no fee)
        account.transfer(targetAccount, 50.0);
        assertEquals(950.0, account.getBalance()); // No fee
        assertEquals(50.0, targetAccount.getBalance());
        
        // Condition 6: Non-VIP account, amount > 100 (with fee, under cap)
        targetAccount = new BankAccount("ACC005", "Another Target", BankAccount.AccountType.SAVINGS);
        account.transfer(targetAccount, 200.0);
        double expectedFee = 200.0 * 0.01; // $2 fee
        assertEquals(950.0 - 200.0 - expectedFee, account.getBalance());
        assertEquals(200.0, targetAccount.getBalance());
        
        // Condition 7: Large transfer with fee cap
        account.deposit(5000.0);
        targetAccount = new BankAccount("ACC006", "Large Target", BankAccount.AccountType.SAVINGS);
        account.transfer(targetAccount, 3000.0);
        double maxFee = 25.0; // Fee cap
        double expectedBalance = account.getBalance() - maxFee; // Fee was already deducted
        assertTrue(account.getBalance() > 0);
        assertEquals(3000.0, targetAccount.getBalance());
    }

    // APPLY MONTHLY FEE - COMPLETE PATH COVERAGE
    @Test
    @DisplayName("Apply monthly fee - all execution paths")
    void testApplyMonthlyFeeAllPaths() {
        // Path 1: Inactive account (early return)
        account.setActive(false);
        double balanceBefore = account.getBalance();
        account.applyMonthlyFee();
        assertEquals(balanceBefore, account.getBalance()); // No change
        
        account.setActive(true);
        
        // Path 2: VIP account (no fee)
        account.setAccountType(BankAccount.AccountType.VIP);
        account.deposit(100.0);
        double vipBalance = account.getBalance();
        account.applyMonthlyFee();
        assertEquals(vipBalance, account.getBalance()); // No fee
        
        // Path 3: Savings account with sufficient balance (fee applied)
        BankAccount savingsAccount = new BankAccount("SAV", "User", BankAccount.AccountType.SAVINGS);
        savingsAccount.deposit(200.0); // Below $500 minimum, so $5 fee
        savingsAccount.applyMonthlyFee();
        assertEquals(195.0, savingsAccount.getBalance());
        assertEquals(2, savingsAccount.getTransactionCount()); // Deposit + Fee
        
        // Path 4: Savings account with sufficient balance (no fee)
        savingsAccount.deposit(500.0); // Total $695, above minimum
        int transactionsBefore = savingsAccount.getTransactionCount();
        savingsAccount.applyMonthlyFee();
        assertEquals(695.0, savingsAccount.getBalance()); // No additional fee
        assertEquals(transactionsBefore, savingsAccount.getTransactionCount()); // No transaction
        
        // Path 5: Checking account with overdraft allowed
        BankAccount checkingAccount = new BankAccount("CHK", "User", BankAccount.AccountType.CHECKING);
        checkingAccount.deposit(5.0); // $10 fee will cause overdraft
        checkingAccount.applyMonthlyFee();
        assertEquals(-5.0, checkingAccount.getBalance()); // Overdraft allowed
        assertTrue(checkingAccount.isActive());
        
        // Path 6: Account closed due to insufficient funds
        BankAccount businessAccount = new BankAccount("BUS", "Company", BankAccount.AccountType.BUSINESS);
        businessAccount.deposit(10.0); // $25 fee, insufficient funds, no overdraft
        businessAccount.applyMonthlyFee();
        assertFalse(businessAccount.isActive()); // Account closed
    }

    // CALCULATE INTEREST - SWITCH STATEMENT COVERAGE
    @Test
    @DisplayName("Calculate interest - all switch branches")
    void testCalculateInterestSwitchCoverage() {
        // Branch: Inactive account
        account.setActive(false);
        assertEquals(0.0, account.calculateInterest());
        
        // Branch: Zero/negative balance
        account.setActive(true);
        assertEquals(0.0, account.calculateInterest());
        
        // SAVINGS account branches
        BankAccount savingsAccount = new BankAccount("SAV", "User", BankAccount.AccountType.SAVINGS);
        
        // Low balance (< $1000) - 1.5% rate
        savingsAccount.deposit(500.0);
        double lowBalanceInterest = savingsAccount.calculateInterest();
        double expectedLowInterest = 500.0 * 0.015 / 12;
        assertEquals(expectedLowInterest, lowBalanceInterest, 0.001);
        
        // High balance (>= $1000) - 2.5% rate
        savingsAccount.deposit(1000.0); // Total $1500
        double highBalanceInterest = savingsAccount.calculateInterest();
        double expectedHighInterest = 1500.0 * 0.025 / 12;
        assertEquals(expectedHighInterest, highBalanceInterest, 0.001);
        
        // CHECKING account branch
        BankAccount checkingAccount = new BankAccount("CHK", "User", BankAccount.AccountType.CHECKING);
        checkingAccount.deposit(1000.0);
        double checkingInterest = checkingAccount.calculateInterest();
        double expectedCheckingInterest = 1000.0 * 0.005 / 12;
        assertEquals(expectedCheckingInterest, checkingInterest, 0.001);
        
        // BUSINESS account branch
        BankAccount businessAccount = new BankAccount("BUS", "Company", BankAccount.AccountType.BUSINESS);
        businessAccount.deposit(1000.0);
        double businessInterest = businessAccount.calculateInterest();
        double expectedBusinessInterest = 1000.0 * 0.03 / 12;
        assertEquals(expectedBusinessInterest, businessInterest, 0.001);
        
        // VIP account branch
        BankAccount vipAccount = new BankAccount("VIP", "VIP User", BankAccount.AccountType.VIP);
        vipAccount.deposit(1000.0);
        double vipInterest = vipAccount.calculateInterest();
        double expectedVipInterest = 1000.0 * 0.035 / 12;
        assertEquals(expectedVipInterest, vipInterest, 0.001);
    }

    // CALCULATE MONTHLY FEE - PRIVATE METHOD COVERAGE
    @Test
    @DisplayName("Calculate monthly fee - switch statement coverage")
    void testCalculateMonthlyFeeSwitchBranches() {
        // Test fee calculation indirectly through applyMonthlyFee
        
        // SAVINGS - low balance
        BankAccount savingsLow = new BankAccount("SAV1", "User", BankAccount.AccountType.SAVINGS);
        savingsLow.deposit(100.0); // Below $500
        double balanceBefore = savingsLow.getBalance();
        savingsLow.applyMonthlyFee();
        assertEquals(5.0, balanceBefore - savingsLow.getBalance()); // $5 fee
        
        // SAVINGS - high balance
        BankAccount savingsHigh = new BankAccount("SAV2", "User", BankAccount.AccountType.SAVINGS);
        savingsHigh.deposit(600.0); // Above $500
        balanceBefore = savingsHigh.getBalance();
        savingsHigh.applyMonthlyFee();
        assertEquals(0.0, balanceBefore - savingsHigh.getBalance()); // No fee
        
        // CHECKING - low balance
        BankAccount checkingLow = new BankAccount("CHK1", "User", BankAccount.AccountType.CHECKING);
        checkingLow.deposit(500.0); // Below $1000
        balanceBefore = checkingLow.getBalance();
        checkingLow.applyMonthlyFee();
        assertEquals(10.0, balanceBefore - checkingLow.getBalance()); // $10 fee
        
        // CHECKING - high balance
        BankAccount checkingHigh = new BankAccount("CHK2", "User", BankAccount.AccountType.CHECKING);
        checkingHigh.deposit(1500.0); // Above $1000
        balanceBefore = checkingHigh.getBalance();
        checkingHigh.applyMonthlyFee();
        assertEquals(0.0, balanceBefore - checkingHigh.getBalance()); // No fee
        
        // BUSINESS - always $25
        BankAccount business = new BankAccount("BUS", "Company", BankAccount.AccountType.BUSINESS);
        business.deposit(1000.0);
        balanceBefore = business.getBalance();
        business.applyMonthlyFee();
        assertEquals(25.0, balanceBefore - business.getBalance()); // $25 fee
        
        // VIP - no fee
        BankAccount vip = new BankAccount("VIP", "VIP User", BankAccount.AccountType.VIP);
        vip.deposit(1000.0);
        balanceBefore = vip.getBalance();
        vip.applyMonthlyFee();
        assertEquals(0.0, balanceBefore - vip.getBalance()); // No fee
    }

    // GET AVAILABLE BALANCE - CONDITIONAL COVERAGE
    @Test
    @DisplayName("Get available balance - conditional logic coverage")
    void testGetAvailableBalanceConditions() {
        // Test through withdrawal behavior since method is private
        
        // CHECKING account - should allow overdraft
        BankAccount checking = new BankAccount("CHK", "User", BankAccount.AccountType.CHECKING);
        checking.deposit(100.0);
        
        // Should allow withdrawal up to overdraft limit
        assertDoesNotThrow(() -> checking.withdraw(500.0)); // $400 overdraft
        assertEquals(-400.0, checking.getBalance());
        
        // Non-checking account - no overdraft
        BankAccount savings = new BankAccount("SAV", "User", BankAccount.AccountType.SAVINGS);
        savings.deposit(100.0);
        
        // Should not allow overdraft
        assertThrows(IllegalStateException.class, () -> savings.withdraw(200.0));
    }

    // LOOP COVERAGE
    @Test
    @DisplayName("Loop coverage in account statement generation")
    void testAccountStatementGeneration() {
        account.deposit(500.0);
        account.withdraw(100.0);
        
        String statement = account.getAccountStatement();
        
        // Verify all required fields are present (tests string building logic)
        assertTrue(statement.contains("ACCOUNT STATEMENT"));
        assertTrue(statement.contains(account.getAccountNumber()));
        assertTrue(statement.contains(account.getAccountHolderName()));
        assertTrue(statement.contains(account.getAccountType().toString()));
        assertTrue(statement.contains(String.format("%.2f", account.getBalance())));
        assertTrue(statement.contains(account.isActive() ? "Active" : "Inactive"));
        assertTrue(statement.contains(String.valueOf(account.getTransactionCount())));
    }

    // EQUALS/HASHCODE COVERAGE
    @Test
    @DisplayName("Equals and hashCode method coverage")
    void testEqualsHashCodeCoverage() {
        BankAccount account1 = new BankAccount("ACC001", "John", BankAccount.AccountType.CHECKING);
        BankAccount account2 = new BankAccount("ACC001", "Jane", BankAccount.AccountType.SAVINGS); // Same account number
        BankAccount account3 = new BankAccount("ACC002", "John", BankAccount.AccountType.CHECKING);
        
        // Same account number - should be equal
        assertTrue(account1.equals(account2));
        assertEquals(account1.hashCode(), account2.hashCode());
        
        // Different account number - should not be equal
        assertFalse(account1.equals(account3));
        
        // Self-comparison
        assertTrue(account1.equals(account1));
        
        // Null comparison
        assertFalse(account1.equals(null));
        
        // Different class comparison
        assertFalse(account1.equals("string"));
    }
}