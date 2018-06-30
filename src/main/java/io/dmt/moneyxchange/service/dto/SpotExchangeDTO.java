package io.dmt.moneyxchange.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import io.dmt.moneyxchange.domain.enumeration.Operation;

/**
 * A DTO for the SpotExchange entity.
 */
public class SpotExchangeDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant fromInstant;

    @NotNull
    private Operation operation;

    private Long sourceCurrencyId;

    private String sourceCurrencyCode;

    private Long targetCurrencyId;

    private String targetCurrencyCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFromInstant() {
        return fromInstant;
    }

    public void setFromInstant(Instant fromInstant) {
        this.fromInstant = fromInstant;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Long getSourceCurrencyId() {
        return sourceCurrencyId;
    }

    public void setSourceCurrencyId(Long currencyId) {
        this.sourceCurrencyId = currencyId;
    }

    public String getSourceCurrencyCode() {
        return sourceCurrencyCode;
    }

    public void setSourceCurrencyCode(String currencyCode) {
        this.sourceCurrencyCode = currencyCode;
    }

    public Long getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(Long currencyId) {
        this.targetCurrencyId = currencyId;
    }

    public String getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(String currencyCode) {
        this.targetCurrencyCode = currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SpotExchangeDTO spotExchangeDTO = (SpotExchangeDTO) o;
        if(spotExchangeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), spotExchangeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SpotExchangeDTO{" +
            "id=" + getId() +
            ", fromInstant='" + getFromInstant() + "'" +
            ", operation='" + getOperation() + "'" +
            "}";
    }
}
