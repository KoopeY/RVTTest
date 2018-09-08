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

    @Before
    public void init() {
        logger.info("Start tests");
        Injector injector = Guice.createInjector(new DiModule());
        injector.getInstance(TestAccountApplication.class);
        em = Persistence.createEntityManagerFactory("ACCOUNT").createEntityManager();
    }

    @After
    public void finish() {
        em.close();
        logger.info("Finish tests");
    }

    @Test
    public void create_accountA_sum100_thenOk() {
        accountService.create("A", 100D);
        Account dbAccount = (Account) em.createQuery("SELECT a FROM Account a WHERE a.account = :account")
                .setParameter("account", "A")
                .getSingleResult();

        Assert.assertEquals(dbAccount.getAccount(), "A");
        Assert.assertEquals(dbAccount.getBalance(), 100D, DELTA);
    }

    @Test
    public void deposit_accountB_sum50_thenOk() throws AccountNotFoundException {
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
}
