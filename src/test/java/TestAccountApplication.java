import model.Account;
import model.exception.AccountOperationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

public class TestAccountApplication {
    private Logger logger = LoggerFactory.getLogger(TestAccountApplication.class);

    private EntityManager em;
    private AccountService accountService = AccountService.getInstance();
    private final Double delta = 0.0001;

    @Before
    public void init() {
        logger.info("Start tests");
        em = Persistence.createEntityManagerFactory("ACCOUNT").createEntityManager();
    }

    @After
    public void finish() {
        em.close();
    }

    @Test
    public void create_accountA_sum100_thenOk() {
        accountService.create("A", 100D);
        Account dbAccount = (Account) em.createQuery("SELECT a FROM Account a WHERE a.account = :account")
                .setParameter("account", "A")
                .getSingleResult();

        Assert.assertEquals(dbAccount.getAccount(), "A");
        Assert.assertEquals(dbAccount.getBalance(), 100.0, delta);
    }

    @Test
    public void deposit_accountA_sum50_thenOk() {
        accountService.deposit("A", 50D);
        Account dbAccount = (Account) em.createQuery("SELECT a FROM Account a WHERE a.account = :account")
                .setParameter("account", "A")
                .getSingleResult();

        Assert.assertEquals(dbAccount.getAccount(), "A");
        Assert.assertEquals(dbAccount.getBalance(), 150.0, delta);
    }
}
