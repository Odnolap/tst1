package com.odnolap.tst1.model;

import com.odnolap.tst1.model.dto.CustomerDto;
import lombok.Data;

import java.util.List;

@Data
public class GetCustomersResponse {
    private List<CustomerDto> customers;
}
