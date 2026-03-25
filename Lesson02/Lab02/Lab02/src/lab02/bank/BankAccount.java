package lab02.bank;

/**
 * BankAccount class for demonstrating blackbox and whitebox testing strategies
 * Includes various business rules and validation logic
 */
public class BankAccount {
    
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private AccountType accountType;
    private boolean isActive;
    private int transactionCount;
    private static final double OVERDRAFT_LIMIT = 500.0;
    private static final double VIP_THRESHOLD = 10000.0;
    
    public enum AccountType {
        SAVINGS, CHECKING, BUSINESS, VIP
    }
    
    public BankAccount(String accountNumber, String accountHolderName, AccountType accountType) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }
        if (accountHolderName == null || accountHolderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be null or empty");
        }
        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
        
        this.accountNumber = accountNumber.trim();
        this.accountHolderName = accountHolderName.trim();
        this.accountType = accountType;
        this.balance = 0.0;
        this.isActive = true;
        this.transactionCount = 0;
    }
    
    /**
     * Deposits money into the account
     * @param amount amount to deposit
     * @throws IllegalArgumentException if amount is invalid
     * @throws IllegalStateException if account is inactive
     */
    public void deposit(double amount) {
        validateAccountActive();
        validatePositiveAmount(amount);
        
        if (amount > 10000 && accountType != AccountType.BUSINESS && accountType != AccountType.VIP) {
            throw new IllegalArgumentException("Large deposits require business or VIP account");
        }
        
        balance += amount;
        transactionCount++;
        
        // Auto-upgrade to VIP if balance exceeds threshold
        if (balance >= VIP_THRESHOLD && accountType == AccountType.SAVINGS) {
            accountType = AccountType.VIP;
        }
    }
    
    /**
     * Withdraws money from the account
     * @param amount amount to withdraw
     * @throws IllegalArgumentException if amount is invalid
     * @throws IllegalStateException if insufficient funds or account inactive
     */
    public void withdraw(double amount) {
        validateAccountActive();
        validatePositiveAmount(amount);
        
        double availableBalance = getAvailableBalance();
        
        if (amount > availableBalance) {
            throw new IllegalStateException("Insufficient funds. Available: $" + availableBalance);
        }
        
        // Business accounts have daily withdrawal limits
        if (accountType == AccountType.BUSINESS && amount > 5000) {
            throw new IllegalArgumentException("Business accounts limited to $5000 per transaction");
        }
        
        balance -= amount;
        transactionCount++;
    }
    
    /**
     * Transfers money to another account
     * @param targetAccount destination account
     * @param amount amount to transfer
     */
    public void transfer(BankAccount targetAccount, double amount) {
        if (targetAccount == null) {
            throw new IllegalArgumentException("Target account cannot be null");
        }
        if (targetAccount.equals(this)) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }
        if (!targetAccount.isActive()) {
            throw new IllegalStateException("Target account is inactive");
        }
        
        // Apply transfer fee for non-VIP accounts
        double transferFee = 0.0;
        if (accountType != AccountType.VIP && amount > 100) {
            transferFee = amount * 0.01; // 1% fee
            if (transferFee > 25.0) {
                transferFee = 25.0; // Max fee cap
            }
        }
        
        double totalAmount = amount + transferFee;
        this.withdraw(totalAmount);
        targetAccount.deposit(amount);
    }
    
    /**
     * Applies monthly maintenance fee
     */
    public void applyMonthlyFee() {
        if (!isActive) {
            return;
        }
        
        double fee = calculateMonthlyFee();
        if (fee > 0) {
            if (balance >= fee) {
                balance -= fee;
                transactionCount++;
            } else {
                // Overdraft or insufficient funds
                if (accountType == AccountType.CHECKING && (balance - fee) >= -OVERDRAFT_LIMIT) {
                    balance -= fee; // Allow overdraft
                    transactionCount++;
                } else {
                    isActive = false; // Close account due to insufficient funds
                }
            }
        }
    }
    
    /**
     * Calculates interest based on account type and balance
     * @return calculated interest amount
     */
    public double calculateInterest() {
        if (!isActive || balance <= 0) {
            return 0.0;
        }
        
        double rate;
        switch (accountType) {
            case SAVINGS:
                rate = balance >= 1000 ? 0.025 : 0.015; // 2.5% or 1.5%
                break;
            case CHECKING:
                rate = 0.005; // 0.5%
                break;
            case BUSINESS:
                rate = 0.03; // 3%
                break;
            case VIP:
                rate = 0.035; // 3.5%
                break;
            default:
                rate = 0.0;
        }
        
        return balance * rate / 12; // Monthly interest
    }
    
    /**
     * Gets account statement information
     * @return formatted account statement
     */
    public String getAccountStatement() {
        StringBuilder statement = new StringBuilder();
        statement.append("=== ACCOUNT STATEMENT ===\n");
        statement.append("Account Number: ").append(accountNumber).append("\n");
        statement.append("Account Holder: ").append(accountHolderName).append("\n");
        statement.append("Account Type: ").append(accountType).append("\n");
        statement.append("Balance: $").append(String.format("%.2f", balance)).append("\n");
        statement.append("Available Balance: $").append(String.format("%.2f", getAvailableBalance())).append("\n");
        statement.append("Status: ").append(isActive ? "Active" : "Inactive").append("\n");
        statement.append("Transactions: ").append(transactionCount).append("\n");
        statement.append("Monthly Interest: $").append(String.format("%.2f", calculateInterest())).append("\n");
        statement.append("========================");
        return statement.toString();
    }
    
    // Utility methods
    private double calculateMonthlyFee() {
        switch (accountType) {
            case SAVINGS:
                return balance < 500 ? 5.0 : 0.0;
            case CHECKING:
                return balance < 1000 ? 10.0 : 0.0;
            case BUSINESS:
                return 25.0;
            case VIP:
                return 0.0;
            default:
                return 0.0;
        }
    }
    
    private double getAvailableBalance() {
        if (accountType == AccountType.CHECKING) {
            return balance + OVERDRAFT_LIMIT;
        }
        return balance;
    }
    
    private void validateAccountActive() {
        if (!isActive) {
            throw new IllegalStateException("Account is inactive");
        }
    }
    
    private void validatePositiveAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new IllegalArgumentException("Amount must be a valid number");
        }
    }
    
    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getAccountHolderName() {
        return accountHolderName;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public int getTransactionCount() {
        return transactionCount;
    }
    
    // For testing purposes
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BankAccount that = (BankAccount) obj;
        return accountNumber.equals(that.accountNumber);
    }
    
    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }
}