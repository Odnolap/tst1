package com.odnolap.tst1.model.db;

import lombok.Data;

@Data
public class Account {
    private Long id;
    private Customer customer;
    private Currency currency;
    private Float balance;
    private boolean isActive;
}
