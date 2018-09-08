package interfaces;

import model.Account;
import model.exception.AccountNotFoundException;
import model.exception.AccountOperationException;

public interface IAccountService {
    Account create(String number, Double balance);
    Account deposit(String number, Double sum) throws AccountNotFoundException;
    Account withdraw(String number, Double sum) throws AccountOperationException, AccountNotFoundException;
    Account transfer(String from, String to, Double sum) throws AccountOperationException, AccountNotFoundException;
}
