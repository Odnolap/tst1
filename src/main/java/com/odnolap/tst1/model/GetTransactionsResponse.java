package com.odnolap.tst1.model;

import com.odnolap.tst1.model.dto.MoneyTransferTransactionDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetTransactionsResponse {
    private List<MoneyTransferTransactionDto> transactions;
}