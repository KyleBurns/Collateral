package com.example.collateral;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;

@Service
public class PositionService {

    private final RestTemplate restTemplate;

    @Value("${position.service.url}")
    private String positionServiceUrl;

    public PositionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Retrieves the positions for a given list of account IDs by calling the external positions service.
     *
     * @param accountIds List of account IDs
     * @return A list of PositionResponse objects representing positions held in each account
     * @throws CollateralServiceException if the service is unreachable, returns an error, or responds with malformed data
     */
    public List<PositionResponse> getPositions(List<String> accountIds) {
        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare the request entity with the list of account IDs
        HttpEntity<List<String>> request = new HttpEntity<>(accountIds, headers);

        // Use ParameterizedTypeReference to retrieve a list
        try {
            ResponseEntity<PositionResponse[]> response = restTemplate.exchange(
                    positionServiceUrl,
                    HttpMethod.POST, 
                    request,
                    PositionResponse[].class
            );
            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new CollateralServiceException("Failed to fetch account position status from position service.");
            }
            return Arrays.asList(response.getBody());
        }
        catch (RestClientException ex) {
            throw new CollateralServiceException("Error while calling position service.", ex);
        }    
    }
}