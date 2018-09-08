package model.exception;

public class AccountNotFoundException extends Exception {
    private final String account;

    public AccountNotFoundException(String account, String message) {
        super(message);
        this.account = account;
    }
}
