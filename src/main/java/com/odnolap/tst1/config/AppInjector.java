package com.odnolap.tst1.config;

import com.google.inject.AbstractModule;
import com.odnolap.tst1.repository.AccountRepository;
import com.odnolap.tst1.repository.AccountRepositoryDb;
import com.odnolap.tst1.repository.CustomerRepository;
import com.odnolap.tst1.repository.CustomerRepositoryDb;
import com.odnolap.tst1.repository.ExchangeRateRepository;
import com.odnolap.tst1.repository.ExchangeRateRepositoryDb;
import com.odnolap.tst1.repository.MoneyTransferRepository;
import com.odnolap.tst1.repository.MoneyTransferRepositoryDb;
import com.odnolap.tst1.service.AccountService;
import com.odnolap.tst1.service.AccountServiceImpl;
import com.odnolap.tst1.service.CustomerService;
import com.odnolap.tst1.service.CustomerServiceImpl;
import com.odnolap.tst1.service.ExchangeRateService;
import com.odnolap.tst1.service.ExchangeRateServiceImpl;
import com.odnolap.tst1.service.MoneyTransferService;
import com.odnolap.tst1.service.MoneyTransferServiceImpl;
import org.hibernate.SessionFactory;

public class AppInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(SessionFactory.class).toInstance(DbConfing.buildSessionFactory());

        bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class);
        bind(MoneyTransferRepository.class).to(MoneyTransferRepositoryDb.class);
        bind(CustomerService.class).to(CustomerServiceImpl.class);
        bind(CustomerRepository.class).to(CustomerRepositoryDb.class);
        bind(AccountService.class).to(AccountServiceImpl.class);
        bind(AccountRepository.class).to(AccountRepositoryDb.class);
        bind(ExchangeRateService.class).to(ExchangeRateServiceImpl.class);
        bind(ExchangeRateRepository.class).to(ExchangeRateRepositoryDb.class);

    }
}
