package com.example.collateral;

import java.util.List;

public record EligibilityRequest(List<String> accountIds, List<String> assetIds) {}