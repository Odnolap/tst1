package com.odnolap.tst1.unitcommon;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.odnolap.tst1.config.AppInjector;
import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class BaseUnitTest {

    protected static Injector injector;

    @BeforeClass
    public static void initEnvironment() {
        injector = Guice.createInjector(new AppInjector());
    }

    @AfterClass
    public static void shutdownEnvironment() {
        SessionFactory sessionFactory = injector.getInstance(SessionFactory.class);
        sessionFactory.close();
    }
}
