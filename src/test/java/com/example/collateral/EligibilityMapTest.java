package com.example.collateral;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EligibilityMapTest {

    @Mock
    private RestTemplate restTemplate;

    private EligibilityService eligibilityService;

    @BeforeEach
    void setUp() {
        eligibilityService = new EligibilityService(restTemplate);
    }

    @Test
    void shouldCalculateMapCorrectly() {
        List<EligibilityResponse> eligResponses = List.of(
            new EligibilityResponse(true, List.of("S1"), List.of("E1"), 0.9),
            new EligibilityResponse(false, List.of("S2"), List.of("E1"), null)
        );
        Map<String, Map<String, Double>> eligibilityMap = eligibilityService.buildEligibilityMap(eligResponses);

        assertEquals(1, eligibilityMap.size());
        assertEquals(2, eligibilityMap.get("E1").size());
        assertEquals(0.9, eligibilityMap.get("E1").get("S1"));
        assertEquals(0, eligibilityMap.get("E1").get("S2"));
    }
}