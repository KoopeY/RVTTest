import com.google.inject.Guice;
import com.google.inject.Injector;
import config.DiModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import process.AccountProcess;

import javax.inject.Inject;

import static spark.Spark.*;

public class AccountApplication {
    private static Logger logger = LoggerFactory.getLogger(AccountApplication.class);

    private final AccountProcess accountProcess;

    @Inject
    AccountApplication(AccountProcess accountProcess) {
        this.accountProcess = accountProcess;
        initMethods();
    }

    private void initMethods() {
        before((request, response) -> {
            logger.info(String.format("Get request ip: %s", request.ip()));
        });

        after((request, response) -> {
            logger.info(String.format("Send response %s", response.body()));
        });

        post("/account/:number/create", accountProcess::create);
        post("/account/:number/deposit", accountProcess::deposit);
        post("/account/:number/withdraw", accountProcess::withdraw);
        post("/account/:from/transfer/:to", accountProcess::transfer);
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new DiModule());
        injector.getInstance(AccountApplication.class);
    }
}
