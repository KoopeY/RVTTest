package model.exception;

import enums.AccountOperationEnum;

public class AccountOperationException extends Exception {
    private final AccountOperationEnum operation;

    public AccountOperationException(AccountOperationEnum operation, String message) {
        super(message);
        this.operation = operation;
    }
}
