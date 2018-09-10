package service;

import enums.AccountOperationEnum;
import interfaces.IAccountOperation;
import model.exception.AccountOperationException;

public class AccountOperation implements IAccountOperation {
    @Override
    public Double deposit(Double balance, Double sum) {
        return balance + sum;
    }

    @Override
    public Double withdraw(Double balance, Double sum) throws AccountOperationException {
        if (sum > balance) {
            throw new AccountOperationException(
                    AccountOperationEnum.WITHDRAW,
                    String.format("Not enough balance: Balance = %f, Need = %f", balance, sum)
            );
        }

        return balance - sum;
    }
}
