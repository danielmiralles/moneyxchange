package io.dmt.moneyxchange.service.dto;

import java.math.BigDecimal;

public class ExchangeResponseDTO {

    private BigDecimal exchangeRate;
    private String operation;
    private int timeout;

    public ExchangeResponseDTO() {
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
