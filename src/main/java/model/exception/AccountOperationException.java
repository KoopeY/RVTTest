package model.exception;

import enums.AccountOperationEnum;

public class AccountOperationException extends Exception {
    private final AccountOperationEnum operation;
    private final String account;

    public AccountOperationException(String account, AccountOperationEnum operation, String message) {
        super(message);
        this.account = account;
        this.operation = operation;
    }
}
