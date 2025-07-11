package com.example.collateral;

import java.util.List;

public record PositionResponse(String accountId, List<PositionDTO> positions) {
   
    public PositionResponse {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("AccountId cannot be null or blank");
        }
    }
}