package com.odnolap.tst1.model.dto;

import com.odnolap.tst1.model.db.Account;
import com.odnolap.tst1.model.db.Currency;
import lombok.Data;

@Data
public class AccountDto {
    private Long id;
    private Long customerId;
    private Currency currency;
    private Float balance;

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
