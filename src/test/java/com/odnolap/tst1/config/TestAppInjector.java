package com.odnolap.tst1.config;

import com.google.inject.AbstractModule;
import com.odnolap.tst1.repository.MoneyTransferRepository;
import com.odnolap.tst1.repository.MoneyTransferRepositoryInMemoryDb;
import com.odnolap.tst1.service.MoneyTransferService;
import com.odnolap.tst1.service.MoneyTransferServiceImpl;

public class TestAppInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class);
        bind(MoneyTransferRepository.class).to(MoneyTransferRepositoryInMemoryDb.class); // Leave this binding when prod config change it to normal repo.
    }
}

