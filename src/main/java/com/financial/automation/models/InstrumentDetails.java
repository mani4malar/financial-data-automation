package com.financial.automation.models;

import java.math.BigDecimal;

public class InstrumentDetails {

    private String id;
    private String name;
    private String isin;
    private BigDecimal unitPrice;

    public InstrumentDetails() {
    }

    public InstrumentDetails(
            String id,
            String name,
            String isin,
            BigDecimal unitPrice) {
        this.id = id;
        this.name = name;
        this.isin = isin;
        this.unitPrice = unitPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "InstrumentDetails{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isin='" + isin + '\'' +
                ", unitPrice=" + unitPrice +
                '}';
    }
}