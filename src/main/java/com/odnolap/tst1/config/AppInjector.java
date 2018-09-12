package com.odnolap.tst1.config;

import com.google.inject.AbstractModule;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.odnolap.tst1.repository.MoneyTransferRepository;
import com.odnolap.tst1.repository.MoneyTransferRepositoryDb;
import com.odnolap.tst1.service.MoneyTransferService;
import com.odnolap.tst1.service.MoneyTransferServiceImpl;
import org.hibernate.SessionFactory;

public class AppInjector extends AbstractModule {

    @Override
    protected void configure() {
        AnnotatedBindingBuilder<SessionFactory> sessionFactoryBind = bind(SessionFactory.class);
        sessionFactoryBind.toInstance(DbConfing.buildSessionFactory());
        sessionFactoryBind.asEagerSingleton();

        bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class);
        bind(MoneyTransferRepository.class).to(MoneyTransferRepositoryDb.class);
        // TODO: other services and repos
    }
}
