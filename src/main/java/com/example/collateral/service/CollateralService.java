package com.example.collateral;

import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

@Service
public class CollateralService {

    private final PositionService positionService;
    private final EligibilityService eligibilityService;
    private final PriceService priceService;

    public CollateralService(PositionService ps, EligibilityService es, PriceService prs) {
        this.positionService = ps;
        this.eligibilityService = es;
        this.priceService = prs;
    }

    /**
     * Calculates the total collateral value for each account based on their positions,
     * asset prices, and eligibility rules.
     *
     * @param accountIds List of account IDs to calculate collateral for
     * @return List of collateral values per account, rounded to 2 decimal places
     * @throws CollateralServiceException if data is invalid or external service fails
     */
    public List<CollateralResponse> calculateCollateral(List<String> accountIds) {

        // Retrieve all required assetIds by iterating through positions of all accounts
        List<PositionResponse> accountsPositions = positionService.getPositions(accountIds);
        HashSet<String> assetIdSet = new HashSet<String>();
        for (PositionResponse positionResponse : accountsPositions) {
            for (PositionDTO position : positionResponse.positions()) {
                assetIdSet.add(position.assetId());
            }
        }
        List<String> assetIds = new ArrayList<String>();
        assetIds.addAll(assetIdSet);

        // Get price of each asset and build Asset objects
        List<PriceResponse> prices = priceService.getPrices(assetIds);
        HashMap<String, Asset> assets = new HashMap<String, Asset>();
        for(PriceResponse price : prices) {
            assets.put(price.assetId(), new Asset(price.assetId(), price.price()));
        }

        // Get eligibility map
        List<EligibilityResponse> eligibility = eligibilityService.getEligibility(accountIds, assetIds);
        Map<String, Map<String, Double>> eligibilityMap = eligibilityService.buildEligibilityMap(eligibility);

        // Build Account and AccountPosition objects from eligilibity map
        HashMap<String, Account> accounts = new HashMap<String, Account>();
        for (PositionResponse positionResponse : accountsPositions) {
            List<AccountPosition> accountPositions = new ArrayList<AccountPosition>();
            for (PositionDTO position : positionResponse.positions()) {
                accountPositions.add(new AccountPosition(
                    assets.get(position.assetId()), 
                    position.quantity(), 
                    eligibilityMap.get(positionResponse.accountId()).get(position.assetId())
                ));
            }
            accounts.put(
                positionResponse.accountId(), 
                new Account(positionResponse.accountId(), 
                accountPositions)
            );
        }

        // Construct response and call getCollateralValue() on each account, rounding to 2 decimal places
        List<CollateralResponse> response = new ArrayList<CollateralResponse>();
        for (String account : accountIds) {
            response.add(new CollateralResponse(account, Math.round(accounts.get(account).getCollateralValue() * 100) / 100));
        }
        return response;
    }
}