package com.odnolap.config;

import com.google.inject.AbstractModule;
import com.odnolap.service.MoneyTransferService;
import com.odnolap.service.MoneyTransferServiceImpl;

public class AppInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class);
    }
}
