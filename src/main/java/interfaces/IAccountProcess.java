package interfaces;

import spark.Request;
import spark.Response;

public interface IAccountProcess {
    String create(Request request, Response response);
    String deposit(Request request, Response response);
    String withdraw(Request request, Response response);
    String transfer(Request request, Response response);
}
