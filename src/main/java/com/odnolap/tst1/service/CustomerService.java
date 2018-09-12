package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetCustomersRequest;
import com.odnolap.tst1.model.GetCustomersResponse;

public interface CustomerService {

    GetCustomersResponse getCustomers(GetCustomersRequest request);
}
