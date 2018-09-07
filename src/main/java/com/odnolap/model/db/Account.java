package com.odnolap.model.db;

import lombok.Data;

@Data
public class Account {
    private Long id;
    private Customer customer;
    private Currency currency;
    private Float balance;
    private boolean isDefault;
    private boolean isActive;
}
