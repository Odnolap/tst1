package com.odnolap.tst1.config;

import com.google.inject.AbstractModule;
import com.odnolap.tst1.repository.MoneyTransferRepository;
import com.odnolap.tst1.repository.MoneyTransferRepositoryDb;
import com.odnolap.tst1.service.MoneyTransferService;
import com.odnolap.tst1.service.MoneyTransferServiceImpl;

public class AppInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class);
        bind(MoneyTransferRepository.class).to(MoneyTransferRepositoryDb.class);
    }
}
