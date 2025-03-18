package lab7;

public class DepositThreadSync implements Runnable {
    private AccountSync account;
    private double money;

    public DepositThreadSync(AccountSync account, double money) {
        this.account = account;
        this.money = money;
    }

    @Override
    public void run() {
        account.deposit(money);
    }
}
