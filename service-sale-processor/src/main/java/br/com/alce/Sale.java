package br.com.alce;

import java.math.BigDecimal;

public class Sale {

    private String userId, saleId;
    private BigDecimal amount;

    public Sale(String userId, String saleId, BigDecimal amount) {
        this.userId = userId;
        this.saleId = saleId;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "userId='" + userId + '\'' +
                ", saleId='" + saleId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
