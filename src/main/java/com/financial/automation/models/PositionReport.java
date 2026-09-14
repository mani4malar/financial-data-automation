package com.financial.automation.models;

import java.math.BigDecimal;

public class PositionReport {

    private String id;
    private String positionId;
    private String isin;
    private BigDecimal quantity;
    private BigDecimal totalPrice;

    public PositionReport() {
    }

    public PositionReport(
            String id,
            String positionId,
            String isin,
            BigDecimal quantity,
            BigDecimal totalPrice) {
        this.id = id;
        this.positionId = positionId;
        this.isin = isin;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "PositionReport{" +
                "id='" + id + '\'' +
                ", positionId='" + positionId + '\'' +
                ", isin='" + isin + '\'' +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}