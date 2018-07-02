package io.dmt.moneyxchange.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

import io.dmt.moneyxchange.domain.enumeration.Operation;

/**
 * A SpotExchange.
 */
@Entity
@Table(name = "spot_exchange")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "spotexchange")
public class SpotExchange implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "from_instant", nullable = false)
    private Instant fromInstant;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "operation", nullable = false)
    private Operation operation;

    @NotNull
    @Column(name = "rate", precision=10, scale=2, nullable = false)
    private BigDecimal rate;

    @ManyToOne(optional = false)
    @NotNull
    private Currency sourceCurrency;

    @ManyToOne(optional = false)
    @NotNull
    private Currency targetCurrency;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFromInstant() {
        return fromInstant;
    }

    public SpotExchange fromInstant(Instant fromInstant) {
        this.fromInstant = fromInstant;
        return this;
    }

    public void setFromInstant(Instant fromInstant) {
        this.fromInstant = fromInstant;
    }

    public Operation getOperation() {
        return operation;
    }

    public SpotExchange operation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public SpotExchange rate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    public SpotExchange sourceCurrency(Currency currency) {
        this.sourceCurrency = currency;
        return this;
    }

    public void setSourceCurrency(Currency currency) {
        this.sourceCurrency = currency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public SpotExchange targetCurrency(Currency currency) {
        this.targetCurrency = currency;
        return this;
    }

    public void setTargetCurrency(Currency currency) {
        this.targetCurrency = currency;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpotExchange spotExchange = (SpotExchange) o;
        if (spotExchange.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), spotExchange.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SpotExchange{" +
            "id=" + getId() +
            ", fromInstant='" + getFromInstant() + "'" +
            ", operation='" + getOperation() + "'" +
            ", rate=" + getRate() +
            "}";
    }
}
