package bg.sofia.uni.fmi.mjt.udemy.exception;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException() {
        super("Not enough money in the account to pay!");
    }

    public InsufficientBalanceException(String s) {
        super(s);
    }

    public InsufficientBalanceException(String s, Throwable cause) {
        super(s, cause);
    }
}
