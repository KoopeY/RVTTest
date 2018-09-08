package process;

import interfaces.IAccountProcess;
import interfaces.IAccountService;
import model.exception.AccountNotFoundException;
import model.exception.AccountOperationException;
import spark.Request;
import spark.Response;

import javax.inject.Inject;

public class AccountProcess implements IAccountProcess {
    private final IAccountService accountService;

    @Inject
    AccountProcess(IAccountService accountService) {
        this.accountService = accountService;
    }

    public String create(Request request, Response response) {
        String number = request.params("number");
        Double sum = Double.parseDouble(request.queryParams("sum"));
        response.status(201);
        return accountService.create(number, sum).toString();
    }

    public String deposit(Request request, Response response) {
        String number = request.params("number");
        Double sum = Double.parseDouble(request.queryParams("sum"));
        response.status(200);
        try {
            return accountService.deposit(number, sum).toString();
        } catch (AccountNotFoundException e) {
            response.status(404);
            return String.format("Catch exception: %s", e.getMessage());
        }
    }

    public String withdraw(Request request, Response response) {
        String number = request.params("number");
        Double sum = Double.parseDouble(request.queryParams("sum"));

        try {
            response.status(200);
            return accountService.withdraw(number, sum).toString();
        } catch (AccountOperationException e) {
            response.status(400);
            return String.format("Catch exception: %s", e.getMessage());
        } catch (AccountNotFoundException e) {
            response.status(404);
            return String.format("Catch exception: %s", e.getMessage());
        }
    }

    public String transfer(Request request, Response response) {
        String from = request.params("from");
        String to = request.params("to");
        Double sum = Double.parseDouble(request.queryParams("sum"));
        try {
            response.status(200);
            return accountService.transfer(from, to, sum).toString();
        } catch (AccountOperationException e) {
            response.status(400);
            return String.format("Catch exception: %s", e.getMessage());
        } catch (AccountNotFoundException e) {
            response.status(404);
            return String.format("Catch exception: %s", e.getMessage());
        }
    }
}
