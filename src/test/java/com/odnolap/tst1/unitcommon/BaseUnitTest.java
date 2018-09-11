package com.odnolap.tst1.unitcommon;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.odnolap.tst1.helper.db.DbHelper;
import com.odnolap.tst1.repository.MoneyTransferRepository;
import com.odnolap.tst1.service.MoneyTransferService;
import com.odnolap.tst1.service.MoneyTransferServiceImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class BaseUnitTest {

    protected static Injector injector;

    @BeforeClass
    public static void initEnvironment() {
//        DbHelper.initDb();
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class);
                bind(MoneyTransferRepository.class).to(MoneyTransferRepositoryMock.class);
            }
        });
    }

    @AfterClass
    public static void shutdownEnvironment() {
//        DbHelper.shutdownDb();
    }
}
