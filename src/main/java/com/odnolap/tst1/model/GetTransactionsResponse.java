package com.odnolap.tst1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.odnolap.tst1.model.dto.MoneyTransferTransactionDto;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GetTransactionsResponse {
    private List<MoneyTransferTransactionDto> transactions;
}