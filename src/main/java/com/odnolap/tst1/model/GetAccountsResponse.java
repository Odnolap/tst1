package com.odnolap.tst1.model;

import com.odnolap.tst1.model.dto.AccountDto;
import lombok.Data;

import java.util.List;

@Data
public class GetAccountsResponse {
    private List<AccountDto> accounts;
}
