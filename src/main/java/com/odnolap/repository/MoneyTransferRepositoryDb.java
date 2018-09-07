package com.odnolap.repository;

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
public class MoneyTransferRepositoryDb implements MoneyTransferRepository {

    @Override
    public List<MoneyTransferTransaction> getTransaction(Long transactionId) {
        ArrayList<MoneyTransferTransaction> result = new ArrayList<>();
        // TODO
        // Just for test
        float eurToGbpTestRate = 0.87f;
        MoneyTransferTransaction transaction = new MoneyTransferTransaction();
        transaction.setAccountFromId(transactionId * 5);
        transaction.setAccountToId(transactionId * 2);
        transaction.setAmountFrom((float)(transactionId % 1000));
        transaction.setAmountTo((transactionId % 1000) * eurToGbpTestRate);
        transaction.setCurrencyFrom(Currency.EUR);
        transaction.setCurrencyTo(Currency.GBP);
        transaction.setCreationTimestamp(System.currentTimeMillis() - 1000000);
        transaction.setFinalizationTimestamp(System.currentTimeMillis() - 900000);
        transaction.setDescription("Test Transaction (by id)");
        transaction.setStatus(MoneyTransferTransactionStatus.SUCCESFUL);
        transaction.setId(transactionId);
        ExchangeRate exchangeRate = new ExchangeRate(122L, Currency.EUR, Currency.GBP, eurToGbpTestRate, System.currentTimeMillis() - 10000000, null);
        transaction.setExchangeRate(exchangeRate);

        result.add(transaction);

        return result;

    }

    @Override
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
        transaction.setDescription("Test Transaction (by account id)");
        transaction.setStatus(MoneyTransferTransactionStatus.SUCCESFUL);
        transaction.setId(accountId * 4);
        ExchangeRate exchangeRate = new ExchangeRate(122L, Currency.EUR, Currency.GBP, eurToGbpTestRate, System.currentTimeMillis() - 10000000, null);
        transaction.setExchangeRate(exchangeRate);

        result.add(transaction);

        return result;
    }

    @Override
    public List<MoneyTransferTransaction> getCustomerTransactions(Long customerId) {
        ArrayList<MoneyTransferTransaction> result = new ArrayList<>();
        // TODO
        // Just for test
        float eurToGbpTestRate = 0.87f;
        MoneyTransferTransaction transaction = new MoneyTransferTransaction();
        transaction.setAccountFromId(customerId * 2);
        transaction.setAccountToId(customerId * 4);
        transaction.setAmountFrom((float)(customerId % 1000));
        transaction.setAmountTo((customerId % 1000) * eurToGbpTestRate);
        transaction.setCurrencyFrom(Currency.EUR);
        transaction.setCurrencyTo(Currency.GBP);
        transaction.setCreationTimestamp(System.currentTimeMillis() - 1000000);
        transaction.setFinalizationTimestamp(System.currentTimeMillis() - 900000);
        transaction.setDescription("Test Transaction (by customer id)");
        transaction.setStatus(MoneyTransferTransactionStatus.SUCCESFUL);
        transaction.setId(customerId * 4);
        ExchangeRate exchangeRate = new ExchangeRate(122L, Currency.EUR, Currency.GBP, eurToGbpTestRate, System.currentTimeMillis() - 10000000, null);
        transaction.setExchangeRate(exchangeRate);

        result.add(transaction);

        return result;
    }

}
