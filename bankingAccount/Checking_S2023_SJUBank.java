package bankingAccount;

public class Checking_S2023_SJUBank extends Account_S2023_SJUBank {
  /* Starting Balance of the Checking account */
  // thanks to the superclass, balance is available //private double Balance;
  /**
   * Overdraft is possible but only once. To perform a new one it is needed to
   * have a positive balance.
   */
  private int overdraftcounter;
  /**
   * This is the fee applied when the account gets overdrafted.
   */
  private double overdraftFee;
  /**
   *  ATM withdrawal fee 
   */
  private double WithdrawalsFee;

  /** Constructor */
  Checking_S2023_SJUBank() {
    try {
      this.setBalance(0);
    } catch (InvalidAmountException e) {
      this.Balance = 0;
    }
    this.setOverdraftcounter(0);
    this.setOverdraftFee(2);
    this.setWithdrawalsFee(1);
    System.out.printf("Checking account created.\n"); 
  }
  
  /** Constructor 
   * 
   * @param balance
   * This is the initial amount set at the creation of the checking account.
   */
  Checking_S2023_SJUBank(double balance) {
    try {
      this.setBalance(balance);
    } catch (InvalidAmountException e) {
      System.out.println("Exception: " + e);
    }
    this.setOverdraftcounter(0);
    this.setOverdraftFee(2);
    this.setWithdrawalsFee(1);
    System.out.printf("Checking account created.\n");
  }

  /* overdraftFee Getter and Setter */
  public double getOverdraftFee() {
    return overdraftFee;
  }

  public void setOverdraftFee(double overdraftFee) {
    this.overdraftFee = overdraftFee;
  }

  /* Overdraftcounter getter and setter */
  public int getOverdraftcounter() {
    return overdraftcounter;
  }

  public void setOverdraftcounter(int overdraftcounter) {
    this.overdraftcounter = overdraftcounter;
  }

  /* WithdrawalsFee getter and setter */
  public double getWithdrawalsFee() {
    return WithdrawalsFee;
  }

  public void setWithdrawalsFee(double withdrawalsFee) {
    WithdrawalsFee = withdrawalsFee;
  }

  /** Method to apply withdrawals fee. */
  public void applyWithdrawalsFee() {
    System.out.println("Applying withdrawal fee (" + this.getWithdrawalsFee() + "$)");
    Balance = Balance - this.getWithdrawalsFee();
    System.out.printf("Current balance after withdrawal: %.2f\n", this.getBalance());
  }

  /** Method to apply overdraft fee. */
  public void applyOverdraftFee() {
    System.out.println("Applying overdraft fee (" + this.getOverdraftFee() + "$)");
    Balance = Balance - this.getOverdraftFee();
    System.out.printf("Current balance after overdraft: %.2f\n", this.getBalance());
  }

  /**
   * Withdraw method.
   * 
   * @param amount
   * This is the amount that the user wants to withdraw from the checking account.
   * It is possible to perform as many withdrawals as wanted, until the amount and/or the 
   * overdraft constraints allow it.
   * 
   * @see InvalidAmountException
   * 
   * @see OverdraftAccountException
   *
   * @throws InvalidAmountException
   * Exception thrown when the amount is not correct in the context..
   * 
   * @throws OverdraftAccountException
   * Exception thrown when the account is overdrafted and a withdrawal is requested.
   */
  @Override
  public void withdraw(double amount) throws InvalidAmountException, OverdraftAccountException {
    if (amount <= 0) {
      throw new InvalidAmountException(amount);
    }
    System.out.printf("Amount selected: %.2f\n", amount);
    // checking if account will be overdrawn by withdrawal
    if (Balance - amount < 0 && overdraftcounter == 0) { 

      // final double fee = 2; //fee imposed to the first overdraft transaction
      System.out.println("Insufficient funds, a two dollar fee will be levied.");
      // first overdraft transaction is allowed but a two dollar fee is imposed.
      Balance = Balance - amount; 
      this.applyWithdrawalsFee();
      this.applyOverdraftFee();
      overdraftcounter++; // mark the overdraft
      // if not overdraft, proceed with the withdrawal
    } else if (Balance - amount >= 0 && overdraftcounter == 0) { 
      System.out.printf("Withdrawn amount: %.2f\n", amount);
      Balance = Balance - amount;
      this.applyWithdrawalsFee();
      if (this.getBalance() < 0) {
        this.applyOverdraftFee();
        this.overdraftcounter++;

      }
      // last possible case, if overdraft, cannot proceed with withdrawal
    } else if (overdraftcounter != 0) { 

      throw new OverdraftAccountException(this.getOverdraftcounter());

    }
  }

  /**
   * Deposit method.
   * 
   * @param amount 
   * This is the amount that the owner wants to deposit into the checking account.
   *
   * @throws InvalidAmountException
   * Exception thrown when the amount is not correct in the context.
   */
  @Override
  public void deposit(double amount) throws InvalidAmountException {
    if (amount <= 0) {
      throw new InvalidAmountException(amount);
    }
    System.out.println("Depositing...");
    Balance = Balance + amount;
    System.out.printf("Deposited amount: %.2f\n", amount);
    if (Balance >= 0) { //
      if (this.getOverdraftcounter() > 0)
        System.out.println("Overdraft restored.");
      this.setOverdraftcounter(0);
    }

  }

  /**
   * Method to perform payment with checking account.
   * 
   * @param amount
   * This is the amount that the payment requires.
   * 
   * @see InvalidAmountException
   * 
   * @see OverdraftAccountException
   *
   * @throws InvalidAmountException
   * Exception thrown when the amount is not correct in the context.
   *
   * @throws OverdraftAccountException
   * Exception thrown when the account is overdrafted and a withdrawal is requested.
   */
  public void makePayment(double amount) throws InvalidAmountException, OverdraftAccountException {
    if (amount <= 0) {
      throw new InvalidAmountException(amount);
    }
    if (this.getOverdraftcounter() != 0) {
      throw new OverdraftAccountException(this.getOverdraftcounter());
    }

    else if (this.Balance - amount < 0 && this.getOverdraftcounter() == 0) {
      this.Balance -= amount;
      System.out.println("Insufficient funds, a two dollar fee will be levied.");
      this.applyOverdraftFee();
      overdraftcounter++; // mark the overdraft
      System.out.printf("Payment completed. New balance: %.2f\n", this.getBalance());
      return;
    } else {
      this.Balance -= amount;
      System.out.printf("Payment completed. New balance: %.2f\n", this.getBalance());
    }
  }

}