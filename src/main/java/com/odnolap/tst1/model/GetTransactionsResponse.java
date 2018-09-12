package com.odnolap.tst1.model;

import com.odnolap.tst1.model.dto.MoneyTransferTransactionDto;
import lombok.Data;

import java.util.List;

@Data
public class GetTransactionsResponse {
    private List<MoneyTransferTransactionDto> transactions;
}