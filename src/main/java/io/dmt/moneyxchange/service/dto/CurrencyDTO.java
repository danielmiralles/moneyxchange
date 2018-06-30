package io.dmt.moneyxchange.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Currency entity.
 */
public class CurrencyDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 3)
    private String code;

    @NotNull
    @Size(min = 1, max = 2)
    private String symbol;

    @NotNull
    private Boolean symbolLeft;

    @NotNull
    @Size(max = 100)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Boolean isSymbolLeft() {
        return symbolLeft;
    }

    public void setSymbolLeft(Boolean symbolLeft) {
        this.symbolLeft = symbolLeft;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CurrencyDTO currencyDTO = (CurrencyDTO) o;
        if(currencyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), currencyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CurrencyDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", symbolLeft='" + isSymbolLeft() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
