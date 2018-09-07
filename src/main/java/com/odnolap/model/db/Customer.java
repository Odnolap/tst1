package com.odnolap.model.db;

import lombok.Data;

@Data
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
    private String patronymicName;
    private Country country;
    private String idCardNumber;
    private String address;
    private boolean isActive;
}
