package com.odnolap.tst1.helper.db;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class InMemoryDbHelper {
    private static SessionFactory sessionFactory;
    @Getter
    private static Session session;

    public static void initDb() {
        sessionFactory = buildSessionFactory();
        session = sessionFactory.openSession(); // It's used only 1 session for test purposes.
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
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception ex) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
            throw new ExceptionInInitializerError("Initial SessionFactory failed" + ex);
        }

        return sessionFactory;
    }
}
