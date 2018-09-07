package com.odnolap.config;

import com.google.inject.AbstractModule;
import com.odnolap.repository.MoneyTransferRepository;
import com.odnolap.repository.MoneyTransferRepositoryDb;
import com.odnolap.service.MoneyTransferService;
import com.odnolap.service.MoneyTransferServiceImpl;

public class AppInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class);
        bind(MoneyTransferRepository.class).to(MoneyTransferRepositoryDb.class);
    }
}
