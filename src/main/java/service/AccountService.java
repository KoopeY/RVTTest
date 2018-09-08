package service;

import model.Account;
import model.exception.AccountOperationException;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class AccountService {
    private static AccountService accountService = null;
    private EntityManager em = Persistence.createEntityManagerFactory("ACCOUNT").createEntityManager();

    public static AccountService getInstance() {
        if (accountService == null) {
            synchronized (AccountService.class) {
                if (accountService == null) {
                    accountService = new AccountService();
                }
            }
        }

        return accountService;
    }

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

    public Account deposit(String number, Double sum) {
        Account account = getAccount(number);

        if (account != null) {
            em.getTransaction().begin();
            account.deposit(sum);
            em.merge(account);
            em.getTransaction().commit();
        }

        return account;
    }

    public Account withdraw(String number, Double sum) throws AccountOperationException {
        Account account = getAccount(number);

        if (account != null) {
            em.getTransaction().begin();
            account.withdraw(sum);
            em.merge(account);
            em.getTransaction().commit();
        }

        return account;
    }

    public void transfer(String from, String to, Double sum) {

    }

    private Account getAccount(String account) {
        return (Account)em.createQuery("SELECT a FROM Account a WHERE a.account = :account")
                .setParameter("account", account)
                .getSingleResult();
    }
}
