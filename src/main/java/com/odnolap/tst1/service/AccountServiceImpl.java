package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetAccountsRequest;
import com.odnolap.tst1.model.GetAccountsResponse;
import com.odnolap.tst1.model.db.Account;
import com.odnolap.tst1.model.dto.AccountDto;
import com.odnolap.tst1.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Inject
    private AccountRepository accountRepository;
    
    @Override
    public GetAccountsResponse getAccounts(GetAccountsRequest request) {
        GetAccountsResponse result = new GetAccountsResponse();
        List<Account> accounts;
        if (request.getAccountId() != null) {
            accounts = Collections.singletonList(accountRepository.getAccount(request.getAccountId()));
        } else {
            accounts = accountRepository.getAllAccounts(request.getStartFrom(), request.getOffset());
        }

        List<AccountDto> resultAccounts = accounts.stream()
            .filter(Objects::nonNull)
            .map(AccountDto::new)
            .collect(Collectors.toList());
        if (resultAccounts.isEmpty()) {
            log.warn("Account list for get accounts request is empty:\n{}", request);
        }
        result.setAccounts(resultAccounts);

        return result;
    }
}
