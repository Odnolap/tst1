package com.odnolap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odnolap.model.db.MoneyTransferTransactionStatus;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MoneyTransferResponse {
    private Long id;
    private MoneyTransferTransactionStatus status;
}