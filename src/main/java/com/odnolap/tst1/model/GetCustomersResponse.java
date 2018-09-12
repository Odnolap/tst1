package com.odnolap.tst1.model;

import com.odnolap.tst1.model.dto.CustomerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetCustomersResponse {
    private List<CustomerDto> customers;
}
