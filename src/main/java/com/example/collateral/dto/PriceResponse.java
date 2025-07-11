package com.example.collateral;

public record PriceResponse(String assetId, double price) {
    
    public PriceResponse {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative: " + price);
        }
        if (assetId == null || assetId.isBlank()) {
            throw new IllegalArgumentException("AssetId cannot be null or blank");
        }
    }
}