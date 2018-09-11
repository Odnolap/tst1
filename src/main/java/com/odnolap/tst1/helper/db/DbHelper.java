package com.odnolap.tst1.helper.db;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DbHelper {
    private static JdbcDataSource dataSource;
    private static Connection connection;

    public static void initDb() throws SQLException {
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:tst1db;INIT=RUNSCRIPT FROM 'classpath:db/init.sql'");
        dataSource.setUser("sa");
        dataSource.setPassword("sa");
        connection = dataSource.getConnection();
        connection.setAutoCommit(true);
    }

    public static void shutdownDb() throws SQLException {
        connection.close();
        connection = null;
        dataSource = null;
    }
}
