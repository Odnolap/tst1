package com.odnolap.tst1.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NamedQueries({
    @NamedQuery(name = ExchangeRate.ALL,
        query = "SELECT e FROM ExchangeRate e ORDER BY e.validFrom DESC, e.currencyFrom DESC, e.currencyTo DESC "),
    @NamedQuery(name = ExchangeRate.BY_CURRENCIES_AND_DATE,
        query = "SELECT e FROM ExchangeRate e"
            + " WHERE e.currencyFrom = :currencyFrom"
            + " AND e.currencyTo = :currencyTo"
            + " AND e.validFrom <= :dt"
            + " AND (e.validTo IS NULL OR e.validTo > :dt)"
            + " ORDER BY e.validFrom DESC")
})

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exchange_rates")
public class ExchangeRate {
    public static final String ALL = "ExchangeRate.getAll";
    public static final String BY_CURRENCIES_AND_DATE = "ExchangeRate.byCurrenciesAndDate";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "currency_from", nullable = false)
    @NotEmpty
    private Currency currencyFrom;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "currency_to", nullable = false)
    @NotEmpty
    private Currency currencyTo;

    @Column(nullable = false)
    @NotNull
    private Float rate;

    @Column(name = "valid_from", nullable = false)
    @NotNull
    private Date validFrom;

    @Column(name = "valid_to")
    private Date validTo;

    // It's enough to compare 2 records by their id, because it's the primary key
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExchangeRate that = (ExchangeRate) o;
        return id != null
            && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.intValue();
    }
}
