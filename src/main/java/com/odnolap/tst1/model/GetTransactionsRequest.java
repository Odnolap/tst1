package com.odnolap.tst1.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTransactionsRequest extends PageableRequest {
    private Long transactionId;
    private Long accountId;
    private Long customerId;
}
