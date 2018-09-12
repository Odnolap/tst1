package com.odnolap.tst1.repository;

import com.odnolap.tst1.model.db.Customer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
@Slf4j
public class CustomerRepositoryDb implements CustomerRepository {

    @Inject
    private SessionFactory sessionFactory;

    @Override
    public Customer getCustomer(Long customerId) {
        return getCustomer(customerId, null);
    }

    public Customer getCustomer(Long customerId, Session session) {
        log.trace("Getting customer by id: {}", customerId);
        boolean isSessionNew = session == null;
        if (isSessionNew) {
            session = sessionFactory.openSession();
        }
        Customer customer = session.byId(Customer.class).load(customerId);
        if (isSessionNew) {
            session.close();
        }
        return customer;
    }

    @Override
    public Customer getCustomerWithAccounts(Long customerId) {
        Session session = sessionFactory.openSession();
        Customer customer = getCustomer(customerId, session);
        if (customer != null) {
            Hibernate.initialize(customer.getAccounts()); // Load data to lazy field
        }
        session.close();
        return customer;
    }

    @Override
    public List<Customer> getAllCustomers(int startFrom, int offset) {
        log.trace("Getting all customers startFrom={}, offset={}", startFrom, offset);
        Session session = sessionFactory.openSession();
        List<Customer> customerList = session.createNamedQuery(Customer.ALL, Customer.class)
            .setFirstResult(startFrom)
            .setMaxResults(offset)
            .getResultList();
        session.close();
        return customerList;
    }
}
