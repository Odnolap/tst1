package com.odnolap.tst1.helper.db;

import lombok.Getter;
import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.Connection;
import java.sql.SQLException;

public class DbHelper {
    private static SessionFactory sessionFactory;
    @Getter
    private static Session session;

    public static void initDb() {
        sessionFactory = buildSessionFactory();
        session = DbHelper.sessionFactory.openSession(); // It's used 1 connection for test purposes.
    }

    public static void shutdownDb() {
        session.close();
        sessionFactory.close();
    }

    private static SessionFactory buildSessionFactory() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );

            throw new ExceptionInInitializerError("Initial SessionFactory failed" + e);
        }
        return sessionFactory;
    }

/*
    private static JdbcDataSource dataSource;
    @Getter
    private static Connection connection; // Used 1 connection for test purposes.

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
*/


//PreparedStatement ps = connection.prepareStatement("select * from transactions t where t.account_from_id = ?;");
//ps.setLong(1, 11L);
//ps.executeQuery();

//PreparedStatement ps = connection.prepareStatement("select * from exchange_rates;").executeQuery();
//connection.prepareStatement("INSERT INTO exchange_rates (currency_from, currency_to, rate, valid_from, valid_to) values ('USD', 'EUR', 0.88942, truncate(now() + 2), truncate(now() + 3))").execute();
}
