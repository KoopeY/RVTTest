package config;

import com.google.inject.AbstractModule;
import interfaces.IAccountOperation;
import interfaces.IAccountProcess;
import interfaces.IAccountService;
import process.AccountProcess;
import service.AccountOperation;
import service.AccountService;

public class DiModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IAccountService.class).to(AccountService.class);
        bind(IAccountProcess.class).to(AccountProcess.class);
        bind(IAccountOperation.class).to(AccountOperation.class);
    }
}
