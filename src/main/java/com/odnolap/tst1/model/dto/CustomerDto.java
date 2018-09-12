package com.odnolap.tst1.model.dto;

import com.odnolap.tst1.model.db.Country;
import com.odnolap.tst1.model.db.Customer;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class CustomerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String patronymicName;
    private String email;
    private Country country;
    private String idCardNumber;
    private String address;
    private Date registered;
    private List<AccountDto> accounts;

    public CustomerDto(Customer customer) {
        if (customer != null) {
            id = customer.getId();
            firstName = customer.getFirstName();
            lastName = customer.getLastName();
            patronymicName = customer.getPatronymicName();
            email = customer.getEmail();
            country = customer.getCountry();
            idCardNumber = customer.getIdCardNumber();
            address = customer.getAddress();
            registered = customer.getRegistered();
            if (customer.getAccounts() != null) {
                accounts = customer.getAccounts().stream()
                    .filter(Objects::nonNull)
                    .map(AccountDto::new)
                    .collect(Collectors.toList());
            }
        }
    }
}
