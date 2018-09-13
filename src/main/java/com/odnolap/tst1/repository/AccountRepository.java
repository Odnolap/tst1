package com.odnolap.tst1.repository;

import com.odnolap.tst1.model.db.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository {
    Account getAccount(Long accountId);

    List<Account> getAllAccounts(int startFrom, int offset);

    boolean moveMoneyBetweenAccounts(Long accountFromId, BigDecimal amountFrom, Long accountToId, BigDecimal amountTo);
}
