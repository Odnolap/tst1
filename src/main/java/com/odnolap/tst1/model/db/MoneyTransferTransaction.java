package com.odnolap.tst1.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NamedQueries({
    @NamedQuery(name = MoneyTransferTransaction.BY_ACCT_ID,
        query = "SELECT t FROM MoneyTransferTransaction t"
            + " JOIN FETCH t.accountFrom a1"
            + " JOIN FETCH t.accountTo a2"
            + " WHERE a1.id = :accountId OR a2.id = :accountId"
            + " ORDER BY t.creationTimestamp DESC"),
    @NamedQuery(name = MoneyTransferTransaction.BY_CUST_ID,
        query = "SELECT t FROM MoneyTransferTransaction t"
            + " JOIN FETCH t.accountFrom a1"
            + " JOIN FETCH a1.customer c1"
            + " JOIN FETCH t.accountTo a2"
            + " JOIN FETCH a2.customer c2"
            + " WHERE c1.id = :customerId OR c2.id = :customerId"
            + " ORDER BY t.creationTimestamp DESC"),
    @NamedQuery(name = MoneyTransferTransaction.ALL,
        query = "SELECT t FROM MoneyTransferTransaction t"
            + " JOIN FETCH t.accountFrom a1"
            + " JOIN FETCH a1.customer c1"
            + " JOIN FETCH t.accountTo a2"
            + " JOIN FETCH a2.customer c2"
            + " ORDER BY t.creationTimestamp DESC")
})

@Data
@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransferTransaction {
    public static final String BY_ACCT_ID = "MoneyTransferTransaction.getByAcctId";
    public static final String BY_CUST_ID = "MoneyTransferTransaction.getByCustId";
    public static final String ALL = "MoneyTransferTransaction.getAll";

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_from_id", nullable = false)
    private Account accountFrom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_to_id", nullable = false)
    private Account accountTo;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "currency_from", nullable = false)
    @NotEmpty
    private Currency currencyFrom;

    @Column(name = "amount_from", nullable = false)
    @NotNull
    private Float amountFrom;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "currency_to", nullable = false)
    @NotEmpty
    private Currency currencyTo;

    @Column(name = "amount_to", nullable = false)
    @NotNull
    private Float amountTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exchange_rate_id", nullable = false)
    private ExchangeRate exchangeRate;

    @Column(name = "created", nullable = false)
    @NotNull
    @CreationTimestamp
    private Date creationTimestamp = new Date();

    @Column(name = "finalized")
    private Date finalizationTimestamp;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    @NotEmpty
    private MoneyTransferTransactionStatus status;

    @Column
    private String description;

    // It's enough to compare 2 records by their id, because it's the primary key
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoneyTransferTransaction that = (MoneyTransferTransaction) o;
        return id != null
            && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.intValue();
    }
}
