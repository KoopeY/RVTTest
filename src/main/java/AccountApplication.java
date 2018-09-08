import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import process.AccountProcess;

import static spark.Spark.*;

public class AccountApplication {
    private static Logger logger = LoggerFactory.getLogger(AccountApplication.class);

    private AccountApplication() {
        initMethods();
    }

    private void initMethods() {
        before((request, response) -> {
            logger.info(String.format("Get request ip: %s", request.ip()));
        });

        after((request, response) -> {
            logger.info(String.format("Send response %s", response.body()));
        });

        post("/account/:number/create", AccountProcess::create);
        post("/account/:number/deposit", AccountProcess::deposit);
        post("/account/:number/withdraw", AccountProcess::withdraw);
        post("/account/:from/transfer/:to", AccountProcess::transfer);
    }

    public static void main(String[] args) {
        new AccountApplication();
    }
}
