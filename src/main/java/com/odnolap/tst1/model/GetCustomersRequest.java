package com.odnolap.tst1.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetCustomersRequest extends PageableRequest {
    private Long customerId;
}
