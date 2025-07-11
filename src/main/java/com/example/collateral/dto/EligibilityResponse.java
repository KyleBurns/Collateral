package com.example.collateral;

import java.util.List;

public record EligibilityResponse(
    boolean eligible,
    List<String> assetIDs,
    List<String> accountIDs,
    Double discount // nullable
) {}