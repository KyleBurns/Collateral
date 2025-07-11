package com.example.collateral;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InvalidDataTest {

    @Test
    void negativePriceThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new PriceResponse("S1", -5.0);
        });        
        assertTrue(ex.getMessage().contains("Price"));
    }

    @Test
    void negativeQuantityThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new PositionDTO("S1", -5.0);
        });        
        assertTrue(ex.getMessage().contains("Quantity"));
    }

    @Test
    void emptyIdThrowsException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new PositionDTO("", 5.0);
        });        
        assertTrue(ex.getMessage().contains("null"));

        ex = assertThrows(IllegalArgumentException.class, () -> {
            new PriceResponse("", 5.0);
        });        
        assertTrue(ex.getMessage().contains("null"));

        ex = assertThrows(IllegalArgumentException.class, () -> {
            new PositionDTO(null, 5.0);
        });        
        assertTrue(ex.getMessage().contains("null"));

        ex = assertThrows(IllegalArgumentException.class, () -> {
            new PriceResponse(null, 5.0);
        });        
        assertTrue(ex.getMessage().contains("null"));
    }
}