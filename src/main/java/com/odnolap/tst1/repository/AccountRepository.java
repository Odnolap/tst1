package com.odnolap.tst1.repository;

import com.odnolap.tst1.model.db.Account;

import java.util.List;

public interface AccountRepository {
    Account getAccount(Long accountId);

    List<Account> getAllAccounts(int startFrom, int offset);

    boolean moveMoneyBetweenAccounts(Long accountFromId, float amountFrom, Long accountToId, float amountTo);
}
