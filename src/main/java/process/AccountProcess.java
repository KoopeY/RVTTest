package process;

import interfaces.IAccountProcess;
import interfaces.IAccountService;
import lombok.Builder;
import lombok.Data;
import model.exception.AccountNotFoundException;
import model.exception.AccountOperationException;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.concurrent.Callable;

public class AccountProcess implements IAccountProcess {
    private final IAccountService accountService;

    @Inject
    AccountProcess(IAccountService accountService) {
        this.accountService = accountService;
    }

    public String create(Request request, Response response) {
        String number = request.params("number");
        Double sum = Double.parseDouble(request.queryParams("sum"));

        ProcessResult result = process(() -> accountService.create(number, sum));
        response.status(201);
        return result.getMessage();
    }

    public String deposit(Request request, Response response) {
        String number = request.params("number");
        Double sum = Double.parseDouble(request.queryParams("sum"));

        ProcessResult result = process(() -> accountService.deposit(number, sum));
        response.status(result.getStatus());
        return result.getMessage();
    }

    public String withdraw(Request request, Response response) {
        String number = request.params("number");
        Double sum = Double.parseDouble(request.queryParams("sum"));

        ProcessResult result = process(() -> accountService.withdraw(number, sum));
        response.status(result.getStatus());
        return result.getMessage();
    }

    public String transfer(Request request, Response response) {
        String from = request.params("from");
        String to = request.params("to");
        Double sum = Double.parseDouble(request.queryParams("sum"));

        ProcessResult result = process(() -> accountService.transfer(from, to, sum));
        response.status(result.getStatus());
        return result.getMessage();
    }

    private ProcessResult process(Callable callable) {
        try {
            return ProcessResult.builder()
                    .message(callable.call().toString())
                    .status(200)
                    .build();
        } catch (AccountNotFoundException e) {
            return ProcessResult.builder()
                    .message(String.format("Catch exception: %s", e.getMessage()))
                    .status(404)
                    .build();
        } catch (AccountOperationException e) {
                return ProcessResult.builder()
                    .message(String.format("Catch exception: %s", e.getMessage()))
                    .status(400)
                    .build();
        } catch (Exception e) {
            return ProcessResult.builder()
                    .message(String.format("Catch exception: %s", e.getMessage()))
                    .status(500)
                    .build();

        }
    }
}

@Data
@Builder
class ProcessResult {
    private String message;
    private Integer status;
}
