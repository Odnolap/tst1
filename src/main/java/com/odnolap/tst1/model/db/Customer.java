package com.odnolap.tst1.model.db;

import lombok.Data;

@Data
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
    private String patronymicName;
    private String email;
    private Country country;
    private String idCardNumber;
    private String address;
    private Long registeredTimestamp;
    private boolean isActive;
}
