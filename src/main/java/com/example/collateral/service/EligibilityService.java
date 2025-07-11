package com.example.collateral;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class EligibilityService {

    private final RestTemplate restTemplate;

    @Value("${eligibility.service.url}")
    private String eligibilityServiceUrl;

    public EligibilityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches eligibility information for given accounts and assets from the external eligibility service.
     *
     * @param accountIds List of account IDs
     * @param assetIds List of asset IDs
     * @return A list of EligibilityResponse objects representing the eligibility and discount factor of each <Account, Asset> pair
     * @throws CollateralServiceException if the service call fails or the data is invalid
     */
    public List<EligibilityResponse> getEligibility(List<String> accountIds, List<String> assetIds) {
        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the request entity with the list of asset and account IDs
        EligibilityRequest eligibilityRequest = new EligibilityRequest(accountIds, assetIds);
        HttpEntity<EligibilityRequest> request = new HttpEntity<>(eligibilityRequest, headers);

        // Use ParameterizedTypeReference to retrieve a list
        try {
            ResponseEntity<EligibilityResponse[]> response = restTemplate.exchange(
                    eligibilityServiceUrl,
                    HttpMethod.POST, 
                    request,
                    EligibilityResponse[].class
            );
            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new CollateralServiceException("Failed to fetch eligibility status from eligibility service.");
            }
            return Arrays.asList(response.getBody());
        }
        catch (RestClientException ex) {
            throw new CollateralServiceException("Error while calling eligibility service.", ex);
        }        
    }

    /**
     * Builds a nested eligibility map from the raw eligibility service response.
     *
     * The returned map structure is:
     *   accountId → (assetId → discount factor)
     * Ineligible entries are assigned a discount factor of 0.
     *
     * @param responses List of raw eligibility rules returned by the eligibility service
     * @return A map of accountId to a map of eligible assetId and their discount factors
     */
    public Map<String, Map<String, Double>> buildEligibilityMap(List<EligibilityResponse> responses) {
        Map<String, Map<String, Double>> map = new HashMap<>();
        for (EligibilityResponse eligibility : responses) {
            for (String account : eligibility.accountIDs()) {
                map.putIfAbsent(account, new HashMap<>());
                for (String asset : eligibility.assetIDs()) {
                    // If not eligible, set discount factor to zero
                    map.get(account).put(asset, eligibility.eligible() ? eligibility.discount() : 0.0);
                }
            }
        }
        return map;
    }
}