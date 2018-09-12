package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetCustomersRequest;
import com.odnolap.tst1.model.GetCustomersResponse;
import com.odnolap.tst1.model.db.Customer;
import com.odnolap.tst1.model.dto.CustomerDto;
import com.odnolap.tst1.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Inject
    private CustomerRepository customerRepository;
    
    @Override
    public GetCustomersResponse getCustomers(GetCustomersRequest request) {
        GetCustomersResponse result = new GetCustomersResponse();
        List<Customer> customers;
        if (request.getCustomerId() != null) {
            customers = Collections.singletonList(customerRepository.getCustomerWithAccounts(request.getCustomerId()));
        } else {
            customers = customerRepository.getAllCustomers(request.getStartFrom(), request.getOffset());
        }

        List<CustomerDto> resultCustomers = customers.stream()
            .filter(Objects::nonNull)
            .map(CustomerDto::new)
            .collect(Collectors.toList());
        if (resultCustomers.isEmpty()) {
            log.warn("Customer list for get customers request is empty:\n{}", request);
        }
        result.setCustomers(resultCustomers);

        return result;
    }
}
