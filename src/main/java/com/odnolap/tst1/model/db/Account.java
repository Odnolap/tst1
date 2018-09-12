package com.odnolap.tst1.model.db;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NamedQueries({
    @NamedQuery(name = Account.ALL,
        query = "SELECT a FROM Account a"
            + " JOIN FETCH a.customer c"
            + " ORDER BY c.registered DESC, a.balance DESC")
})

@Data
@Entity
@Table(name = "accounts")
public class Account {
    public static final String ALL = "Account.getAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    @NotEmpty
    private Currency currency;

    @Column(nullable = false)
    @NotNull
    private Float balance;

    // It's enough to compare 2 records by their id, because it's the primary key
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account that = (Account) o;
        return id != null
            && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.intValue();
    }

    @Override
    public String toString() {
        return "Account{" +
            "id=" + id +
            ", customerId=" + customer.getId() +
            ", customerName=" + customer.getShortName() +
            ", currency=" + currency +
            ", balance=" + balance +
            '}';
    }
}
