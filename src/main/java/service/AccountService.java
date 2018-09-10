package service;

import enums.AccountOperationEnum;
import interfaces.IAccountService;
import model.Account;
import model.exception.AccountNotFoundException;
import model.exception.AccountOperationException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class AccountService implements IAccountService {
    private final EntityManager em = Persistence.createEntityManagerFactory("ACCOUNT").createEntityManager();
    private final AccountOperation accountOperation;

    @Inject
    AccountService(AccountOperation accountOperation) {
        this.accountOperation = accountOperation;
    }

    /**
     * Create account
     * @param number - account number
     * @param balance - account balance
     * @return operation result
     * @throws AccountOperationException - if account already exists
     */
    public Account create(String number, Double balance) throws AccountOperationException {
        if (existsAccount(number)) {
            throw new AccountOperationException(
                    AccountOperationEnum.CREATE,
                    String.format("Account %s exists", number)
            );
        }

        Account account = Account.builder()
                .account(number)
                .balance(balance)
                .build();

        em.getTransaction().begin();
        em.merge(account);
        em.getTransaction().commit();

        return account;
    }

    /**
     * Deposit to account
     * @param number - account number
     * @param sum - sum to deposit
     * @return operation result
     * @throws AccountNotFoundException - if account does not exist
     */
    public Account deposit(String number, Double sum) throws AccountNotFoundException {
        Account account = getAccount(number);

        em.getTransaction().begin();
        Double newBalance = accountOperation.deposit(account.getBalance(), sum);
        account.setBalance(newBalance);
        em.merge(account);
        em.getTransaction().commit();

        return account;
    }

    /**
     * Withdraw from account
     * @param number - account number
     * @param sum - sum to withdraw
     * @return operation result
     * @throws AccountNotFoundException - if account does not exist
     * @throws AccountOperationException - if balance is not enough to withdraw
     */
    public Account withdraw(String number, Double sum) throws AccountNotFoundException, AccountOperationException {
        Account account = getAccount(number);

        try {
            em.getTransaction().begin();
            Double newBalance = accountOperation.withdraw(account.getBalance(), sum);
            account.setBalance(newBalance);
            em.merge(account);
            em.getTransaction().commit();
        } catch (AccountOperationException e) {
            em.getTransaction().rollback();
            throw new AccountOperationException(AccountOperationEnum.WITHDRAW, e.getMessage());
        }

        return account;
    }

    /**
     * Transfer between accounts
     * @param from - withdraw from account number
     * @param to - deposit to account number
     * @param sum - sum to transfer
     * @return operation result
     * @throws AccountNotFoundException - if account does not exist
     * @throws AccountOperationException - if balance is not enough to withdraw
     */
    public Account transfer(String from, String to, Double sum) throws AccountNotFoundException, AccountOperationException {
        Account fromAccount = getAccount(from);
        Account toAccount = getAccount(to);

        try {
            em.getTransaction().begin();

            Double faNewBalance = accountOperation.withdraw(fromAccount.getBalance(), sum);
            fromAccount.setBalance(faNewBalance);
            em.merge(fromAccount);

            Double taNewBalance = accountOperation.deposit(toAccount.getBalance(), sum);
            toAccount.setBalance(taNewBalance);
            em.merge(toAccount);

            em.getTransaction().commit();
        } catch (AccountOperationException e) {
            em.getTransaction().rollback();
            throw new AccountOperationException(AccountOperationEnum.TRANSFER, e.getMessage());
        }

        return toAccount;
    }

    private Account getAccount(String account) throws AccountNotFoundException {
        Account acc = em.find(Account.class, account);
        if (acc == null) {
            throw new AccountNotFoundException(
                    account,
                    String.format("Not found: account = %s", account)
            );
        }
        return acc;
    }

    private Boolean existsAccount(String account) {
        return em.find(Account.class, account) != null;
    }
}
