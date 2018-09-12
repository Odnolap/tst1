package com.odnolap.tst1.model;

import lombok.Data;

@Data
public class GetTransactionsRequest extends PageableRequest {
    private Long transactionId;
    private Long accountId;
    private Long customerId;
}
