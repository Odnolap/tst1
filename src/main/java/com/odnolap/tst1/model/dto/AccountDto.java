package com.odnolap.tst1.model.dto;

import com.odnolap.tst1.model.db.Account;
import com.odnolap.tst1.model.db.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    private Long id;
    private Long customerId;
    private Currency currency;
    private BigDecimal balance;

    public AccountDto(Account account) {
        if (account != null) {
            id = account.getId();
            if (account.getCustomer() != null) {
                customerId = account.getCustomer().getId();
            }
            currency = account.getCurrency();
            balance = account.getBalance();
        }
    }
}
