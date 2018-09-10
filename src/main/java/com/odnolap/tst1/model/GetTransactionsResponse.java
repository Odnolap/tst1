package com.odnolap.tst1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odnolap.tst1.model.db.MoneyTransferTransaction;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GetTransactionsResponse {
    private List<MoneyTransferTransaction> transactions;
}