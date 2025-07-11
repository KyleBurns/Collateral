package com.example.collateral;

class Asset {

    private final String assetId;
    private final double price;

    public Asset(String assetId, double price) {
        this.assetId = assetId;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}