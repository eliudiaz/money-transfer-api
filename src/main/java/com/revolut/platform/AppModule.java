package com.revolut.platform;

import com.google.inject.servlet.ServletModule;
import com.revolut.repositories.AccountsRepository;
import com.revolut.repositories.AccountsRepositoryImpl;
import com.revolut.repositories.TransactionsLogsRepository;
import com.revolut.repositories.TransactionsLogsRepositoryImpl;
import com.revolut.repositories.WireTransferenceRepository;
import com.revolut.repositories.WireTransferenceRepositoryImpl;
import com.revolut.services.AccountsService;
import com.revolut.services.AccountsServiceImpl;
import com.revolut.services.WireTransferenceService;
import com.revolut.services.WireTransferenceServiceImpl;
import com.revolut.util.DataBaseHelper;

public class AppModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bind(AccountsService.class).to(AccountsServiceImpl.class);
        bind(AccountsRepository.class).to(AccountsRepositoryImpl.class);
        bind(TransactionsLogsRepository.class).to(TransactionsLogsRepositoryImpl.class);
        bind(WireTransferenceRepository.class).to(WireTransferenceRepositoryImpl.class);
        bind(WireTransferenceService.class).to(WireTransferenceServiceImpl.class);
        bind(DataBaseHelper.class);
    }
}
