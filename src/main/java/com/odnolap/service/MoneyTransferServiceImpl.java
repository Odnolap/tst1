package com.odnolap.service;

import com.odnolap.model.db.Currency;
import com.odnolap.model.db.ExchangeRate;
import com.odnolap.model.db.MoneyTransferTransaction;
import com.odnolap.model.db.MoneyTransferTransactionStatus;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Slf4j
public class MoneyTransferServiceImpl implements MoneyTransferService {

    public List<MoneyTransferTransaction> getAccountTransactions(Long accountId) {
        ArrayList<MoneyTransferTransaction> result = new ArrayList<>();
        // TODO
        // Just for test
        float eurToGbpTestRate = 0.87f;
        MoneyTransferTransaction transaction = new MoneyTransferTransaction();
        transaction.setAccountFromId(accountId);
        transaction.setAccountToId(accountId * 2);
        transaction.setAmountFrom((float)(accountId % 1000));
        transaction.setAmountTo((accountId % 1000) * eurToGbpTestRate);
        transaction.setCurrencyFrom(Currency.EUR);
        transaction.setCurrencyTo(Currency.GBP);
        transaction.setCreationTimestamp(System.currentTimeMillis() - 1000000);
        transaction.setFinalizationTimestamp(System.currentTimeMillis() - 900000);
        transaction.setDescription("Test Transaction");
        transaction.setStatus(MoneyTransferTransactionStatus.SUCCESFUL);
        transaction.setId(accountId * 4);
        ExchangeRate exchangeRate = new ExchangeRate(122L, Currency.EUR, Currency.GBP, eurToGbpTestRate, System.currentTimeMillis() - 10000000, null);
        transaction.setExchangeRate(exchangeRate);

        result.add(transaction);

        return result;
    };
}
