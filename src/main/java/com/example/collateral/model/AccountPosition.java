package com.example.collateral;

class AccountPosition {

    private final Asset asset;
    private final double quantity;
    private final double discountFactor;

    public AccountPosition(Asset asset, double quantity, Double discountFactor) {
        this.asset = asset;
        this.quantity = quantity;
        this.discountFactor = discountFactor;
    }

    public double getCollateralValue(){
        return quantity * asset.getPrice() * discountFactor;
    }
}