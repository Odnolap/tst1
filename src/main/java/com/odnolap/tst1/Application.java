package com.odnolap.tst1;

import com.odnolap.tst1.core.MoneyTransferRestServer;
import com.odnolap.tst1.helper.db.dbHelper;

public class Application {

    public static void main(String[] args) {
        dbHelper.initDb();
        MoneyTransferRestServer.startServer();
    }
}
