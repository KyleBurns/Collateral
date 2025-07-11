package com.example.collateral;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;

@Service
public class PriceService {

    private final RestTemplate restTemplate;

    @Value("${price.service.url}")
    private String priceServiceUrl;

    public PriceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches the latest prices for the given set of asset IDs by calling the external price service.
     *
     * @param assetIds Set of asset IDs to fetch prices for
     * @return A list of PriceResponse objects representing prices for each asset
     * @throws CollateralServiceException if the external service fails or returns invalid data (e.g., negative prices)
     */
    public List<PriceResponse> getPrices(List<String> assetIds) {
        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the request entity with the list of asset IDs
        HttpEntity<List<String>> request = new HttpEntity<>(assetIds, headers);

        // Use ParameterizedTypeReference to retrieve a list
        try{
            ResponseEntity<PriceResponse[]> response = restTemplate.exchange(
                    priceServiceUrl,
                    HttpMethod.POST,
                    request,
                    PriceResponse[].class
            );
            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new CollateralServiceException("Failed to fetch price status from price service.");
            }
            return Arrays.asList(response.getBody());
        }
        catch (RestClientException ex) {
            throw new CollateralServiceException("Error while calling price service.", ex);
        }     
    }
}