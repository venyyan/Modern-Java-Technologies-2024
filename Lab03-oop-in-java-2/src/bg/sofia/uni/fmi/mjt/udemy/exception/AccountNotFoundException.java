package bg.sofia.uni.fmi.mjt.udemy.exception;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException() {
        super("Account not found!");
    }

    public AccountNotFoundException(String s) {
        super(s);
    }

    public AccountNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }
}
