package com.odnolap.tst1.model;

import lombok.Data;

@Data
public class GetCustomersRequest extends PageableRequest {
    private Long customerId;
}
