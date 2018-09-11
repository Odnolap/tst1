package com.odnolap.tst1;

import com.odnolap.tst1.core.MoneyTransferRestServer;
import com.odnolap.tst1.helper.db.DbHelper;

import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws SQLException {
        DbHelper.initDb();
        MoneyTransferRestServer.startServer();
    }
}
