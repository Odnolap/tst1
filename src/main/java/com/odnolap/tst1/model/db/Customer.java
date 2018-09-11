package com.odnolap.tst1.model.db;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "first_name", nullable = false)
    @NotEmpty
    @Length(min = 2)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotEmpty
    @Length(min = 2)
    private String lastName;

    @Column(name = "patronymic_name")
    private String patronymicName;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    @NotEmpty
    private Country country;

    @Column(name = "id_card_number", nullable = false, unique = true)
    @NotEmpty
    private String idCardNumber;

    @Column(nullable = false)
    @NotEmpty
    @Length(min = 5)
    private String address;

    @Column(name = "registered", nullable = false)
    @NotNull
    @CreationTimestamp
    private Date registered = new Date();

    @Column(name = "is_active", nullable = false)
    @NotNull
    private boolean isActive = true;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @OrderBy("id ASC")
    private List<Account> accounts;

    // If we use standard equals() method, 2 equal rows can be recognized as non equal because of LAZY fetch type.
    // Moreover, it's enough to compare 2 records by their id, because it's the primary key
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer that = (Customer) o;
        return id != null
            && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.intValue();
    }
}
