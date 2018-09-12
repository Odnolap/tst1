package com.odnolap.tst1.model;

import com.odnolap.tst1.model.dto.AccountDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetAccountsResponse {
    private List<AccountDto> accounts;
}
