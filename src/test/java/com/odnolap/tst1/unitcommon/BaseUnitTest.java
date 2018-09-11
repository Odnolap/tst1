package com.odnolap.tst1.unitcommon;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.odnolap.tst1.config.TestAppInjector;
import com.odnolap.tst1.helper.db.DbHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public abstract class BaseUnitTest {

    protected static Injector injector;

    @BeforeClass
    public static void initEnvironment() {
        DbHelper.initDb();
        injector = Guice.createInjector(new TestAppInjector());
    }

    @AfterClass
    public static void shutdownEnvironment() {
        DbHelper.shutdownDb();
    }
}
