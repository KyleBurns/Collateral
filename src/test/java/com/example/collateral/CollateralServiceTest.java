package com.example.collateral;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CollateralServiceTest {

    @Mock
    private PositionService positionService;

    @Mock
    private EligibilityService eligibilityService;

    @Mock
    private PriceService priceService;

    private CollateralService collateralService;

    @BeforeEach
    void setUp() {
        collateralService = new CollateralService(positionService, eligibilityService, priceService);
    }

    @Test
    void shouldCalculateCollateralCorrectly() {
        List<String> accountIds = List.of("E1");

        // Mock positions
        List<PositionResponse> positions = List.of(
            new PositionResponse("E1", List.of(
                new PositionDTO("S1", 10),
                new PositionDTO("S2", 5)
            ))
        );

        // Mock prices
        List<PriceResponse> prices = List.of(
            new PriceResponse("S1", 100.0),
			new PriceResponse("S2", 50.0)
        );

        // Mock eligibility
        List<EligibilityResponse> eligResponses = List.of(
            new EligibilityResponse(true, List.of("S1"), List.of("E1"), 0.9),
            new EligibilityResponse(false, List.of("S2"), List.of("E1"), null)
        );
        Map<String, Map<String, Double>> eligibilityMap = Map.of(
            "E1", Map.of("S1", 0.9, "S2", 0.0)
        );

        when(positionService.getPositions(accountIds)).thenReturn(positions);
        when(priceService.getPrices(List.of("S1", "S2"))).thenReturn(prices);
        when(eligibilityService.getEligibility(accountIds, List.of("S1", "S2"))).thenReturn(eligResponses);
        when(eligibilityService.buildEligibilityMap(eligResponses)).thenReturn(eligibilityMap);
        List<CollateralResponse> result = collateralService.calculateCollateral(accountIds);

        assertEquals(1, result.size());
        assertEquals("E1", result.get(0).accountId());
        assertEquals(10 * 100 * 0.9, result.get(0).collateralValue());
    }
}