package com.odnolap.model;

import lombok.Data;

@Data
public class GetTransactionsRequest {
    private Long transactionId;
    private Long accountId;
    private Long customerId;
    private int offset;
    private int startFrom;
}
