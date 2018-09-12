package com.odnolap.tst1.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetExchangeRatesRequest extends PageableRequest {
    private Long exchangeRateId;
}
