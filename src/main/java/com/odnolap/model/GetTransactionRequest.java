package com.odnolap.model;

import lombok.Data;

@Data
public class GetTransactionRequest {
    private Long accountId;
    private Long customerId;
}
