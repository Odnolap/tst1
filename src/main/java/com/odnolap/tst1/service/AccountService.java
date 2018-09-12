package com.odnolap.tst1.service;

import com.odnolap.tst1.model.GetAccountsRequest;
import com.odnolap.tst1.model.GetAccountsResponse;

public interface AccountService {

    GetAccountsResponse getAccounts(GetAccountsRequest request);
}
