package com.odnolap.tst1.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAccountsRequest extends PageableRequest {
    private Long accountId;
}
