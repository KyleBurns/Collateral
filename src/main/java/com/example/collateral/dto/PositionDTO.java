package com.example.collateral;

public record PositionDTO(String assetId, double quantity) {

    public PositionDTO {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative: " + quantity);
        }
        if (assetId == null || assetId.isBlank()) {
            throw new IllegalArgumentException("AssetId cannot be null or blank");
        }
    }
}