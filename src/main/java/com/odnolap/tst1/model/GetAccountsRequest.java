package com.odnolap.tst1.model;

import lombok.Data;

@Data
public class GetAccountsRequest extends PageableRequest {
    private Long accountId;
}
