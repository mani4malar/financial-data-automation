package com.financial.automation.models;

import java.math.BigDecimal;

public class PositionDetails {

    private String id;
    private String instrumentId;
    private BigDecimal quantity;

    public PositionDetails() {
    }

    public PositionDetails(
            String id,
            String instrumentId,
            BigDecimal quantity) {
        this.id = id;
        this.instrumentId = instrumentId;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "PositionDetails{" +
                "id='" + id + '\'' +
                ", instrumentId='" + instrumentId + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}