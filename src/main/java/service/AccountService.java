package service;

import interfaces.IAccountService;
import model.Account;
import model.exception.AccountNotFoundException;
import model.exception.AccountOperationException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

public class AccountService implements IAccountService {
    private EntityManager em = Persistence.createEntityManagerFactory("ACCOUNT").createEntityManager();

    public Account create(String number, Double balance) {
        Account account = Account.builder()
                .account(number)
                .balance(balance)
                .build();

        em.getTransaction().begin();
        em.merge(account);
        em.getTransaction().commit();

        return account;
    }

    public Account deposit(String number, Double sum) throws AccountNotFoundException {
        Account account = getAccount(number);

        if (account != null) {
            em.getTransaction().begin();
            account.deposit(sum);
            em.merge(account);
            em.getTransaction().commit();
        }

        return account;
    }

    public Account withdraw(String number, Double sum) throws AccountOperationException, AccountNotFoundException {
        Account account = getAccount(number);

        if (account != null) {
            em.getTransaction().begin();
            account.withdraw(sum);
            em.merge(account);
            em.getTransaction().commit();
        }

        return account;
    }

    public Account transfer(String from, String to, Double sum) throws AccountOperationException, AccountNotFoundException {
        Account fromAccount = getAccount(from);
        Account toAccount = getAccount(to);

        if (fromAccount != null && toAccount != null) {
            em.getTransaction().begin();
            fromAccount.withdraw(sum);
            toAccount.deposit(sum);
            em.getTransaction().commit();
        }

        return toAccount;
    }

    private Account getAccount(String account) throws AccountNotFoundException {
        try {
            return (Account) em.createQuery("SELECT a FROM Account a WHERE a.account = :account")
                    .setParameter("account", account)
                    .getSingleResult();
        } catch(NoResultException e) {
            throw new AccountNotFoundException(account, String.format("Not found: account = %s", account));
        }
    }
}
