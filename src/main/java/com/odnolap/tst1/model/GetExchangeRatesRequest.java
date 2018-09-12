package com.odnolap.tst1.model;

import lombok.Data;

@Data
public class GetExchangeRatesRequest extends PageableRequest {
    private Long exchangeRateId;
}
