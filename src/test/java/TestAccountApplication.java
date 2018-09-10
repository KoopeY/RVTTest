import com.google.inject.Guice;
import com.google.inject.Injector;
import config.DiModule;
import model.Account;
import model.exception.AccountNotFoundException;
import model.exception.AccountOperationException;
import net.lamberto.junit.GuiceJUnitRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountOperation;
import service.AccountService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

@RunWith(GuiceJUnitRunner.class)
public class TestAccountApplication {
    private Logger logger = LoggerFactory.getLogger(TestAccountApplication.class);

    private EntityManager em;
    private final double DELTA = 0.0001;

    @Inject
    private AccountService accountService;

    @Inject
    private AccountOperation accountOperation;

    @Before
    public void init() {
        Injector injector = Guice.createInjector(new DiModule());
        injector.getInstance(TestAccountApplication.class);
        em = Persistence.createEntityManagerFactory("ACCOUNT").createEntityManager();
    }

    @After
    public void finish() {
        em.close();
    }

    @Test
    public void deposit_balance10_plus15_result25_thenOk() {
        Double balance = accountOperation.deposit(10D, 15D);
        Assert.assertEquals(balance, 25D, DELTA);
    }

    @Test
    public void withdraw_balance10_minus5_result5_thenOk() throws AccountOperationException {
        Double balance = accountOperation.withdraw(10D, 5D);
        Assert.assertEquals(balance, 5D, DELTA);
    }

    @Test(expected = AccountOperationException.class)
    public void withdraw_balance10_minus15_thenOperationException() throws AccountOperationException {
        accountOperation.withdraw(10D, 15D);
    }

    @Test
    public void create_accountA_sum100_thenOk() throws AccountOperationException {
        accountService.create("A", 100D);
        Account dbAccount = (Account) em.createQuery("SELECT a FROM Account a WHERE a.account = :account")
                .setParameter("account", "A")
                .getSingleResult();

        Assert.assertEquals(dbAccount.getAccount(), "A");
        Assert.assertEquals(dbAccount.getBalance(), 100D, DELTA);
    }

    @Test
    public void deposit_accountB_sum50_thenOk() throws AccountNotFoundException, AccountOperationException {
        accountService.create("B", 0D);
        accountService.deposit("B", 50D);
        Account dbAccount = (Account) em.createQuery("SELECT a FROM Account a WHERE a.account = :account")
                .setParameter("account", "B")
                .getSingleResult();

        Assert.assertEquals(dbAccount.getAccount(), "B");
        Assert.assertEquals(dbAccount.getBalance(), 50D, DELTA);
    }

    @Test
    public void withdraw_accountC_sum77_thenOk() throws AccountOperationException, AccountNotFoundException {
        accountService.create("C", 150D);
        accountService.withdraw("C", 77D);
        Account dbAccount = (Account) em.createQuery("SELECT a FROM Account a WHERE a.account = :account")
                .setParameter("account", "C")
                .getSingleResult();

        Assert.assertEquals(dbAccount.getAccount(), "C");
        Assert.assertEquals(dbAccount.getBalance(), 73D, DELTA);
    }

    @Test
    public void transfer_accountD_toE_sum25_thenOk() throws AccountOperationException, AccountNotFoundException {
        accountService.create("D", 30D);
        accountService.create("E", 15D);
        accountService.transfer("D", "E", 25D);
        Account fromAccount = (Account) em.createQuery("SELECT a FROM Account a WHERE a.account = :account")
                .setParameter("account", "D")
                .getSingleResult();

        Account toAccount = (Account) em.createQuery("SELECT a FROM Account a WHERE a.account = :account")
                .setParameter("account", "E")
                .getSingleResult();

        Assert.assertEquals(fromAccount.getAccount(), "D");
        Assert.assertEquals(fromAccount.getBalance(), 5D, DELTA);
        Assert.assertEquals(toAccount.getAccount(), "E");
        Assert.assertEquals(toAccount.getBalance(), 40D, DELTA);
    }

    @Test(expected = AccountOperationException.class)
    public void transfer_accountF_toG_sum25_thenOperationException() throws AccountOperationException, AccountNotFoundException {
        accountService.create("F", 30D);
        accountService.create("G", 15D);
        accountService.transfer("F", "G", 40D);
    }

    @Test(expected = AccountNotFoundException.class)
    public void transfer_accountH_toI_sum25_thenNotFoundException() throws AccountOperationException, AccountNotFoundException {
        accountService.create("HI", 15D);
        accountService.transfer("H", "I", 10D);
    }
}
