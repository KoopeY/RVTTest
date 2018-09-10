package interfaces;

import model.exception.AccountOperationException;

public interface IAccountOperation {
    Double deposit(Double balance, Double sum);
    Double withdraw(Double balance, Double sum) throws AccountOperationException;
}
