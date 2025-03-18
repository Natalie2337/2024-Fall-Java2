package lab7;

public class DepositThreadLock implements Runnable {
    private AccountLock account;
    private double money;

    public DepositThreadLock(AccountLock account, double money) {
        this.account = account;
        this.money = money;
    }

    @Override
    public void run() {
        account.deposit(money);
    }
}
