package com.odnolap.repository;

import com.odnolap.model.db.MoneyTransferTransaction;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.List;

@Singleton
@Slf4j
public class MoneyTransferRepositoryDb implements MoneyTransferRepository {

    @Override
    public List<MoneyTransferTransaction> getAccountTransactions(Long accountId) {
        return null;
    }
}
