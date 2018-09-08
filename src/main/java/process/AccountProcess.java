package process;

import model.exception.AccountOperationException;
import service.AccountService;
import spark.Request;
import spark.Response;

public class AccountProcess {
    public static String create(Request request, Response response) {
        String number = request.params("number");
        Double sum = Double.parseDouble(request.queryParams("sum"));
        response.status(201);
        return AccountService.getInstance().create(number, sum).toString();
    }

    public static String deposit(Request request, Response response) {
        String number = request.params("number");
        Double sum = Double.parseDouble(request.queryParams("sum"));
        response.status(200);
        return AccountService.getInstance().deposit(number, sum).toString();
    }

    public static String withdraw(Request request, Response response) {
        String number = request.params("number");
        Double sum = Double.parseDouble(request.queryParams("sum"));

        try {
            response.status(200);
            return AccountService.getInstance().withdraw(number, sum).toString();
        } catch (AccountOperationException e) {
            response.status(400);
            return String.format("Catch exception: %s", e.getMessage());
        }
    }

    public static String transfer(Request request, Response response) {
        String from = request.params("from");
        String to = request.params("to");
        response.status(200);
        return String.format("From %s to %s, sum = %s", from, to, request.queryParams("sum"));
    }
}
