package com.odnolap.tst1.repository;

import com.odnolap.tst1.model.db.Customer;

import java.util.List;

public interface CustomerRepository {
    Customer getCustomer(Long customerId);

    Customer getCustomerWithAccounts(Long customerId);

    List<Customer> getAllCustomers(int startFrom, int offset);
}
