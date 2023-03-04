package com.example.ad.model;

public class TopProduct extends Product{
    private String productName;
    private int purchases;

    public TopProduct(String productName, int purchases) {
        this.productName = productName;
        this.purchases = purchases;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public int getPurchases() {
        return purchases;
    }

    @Override
    public void setPurchases(int purchases) {
        this.purchases = purchases;
    }
}
