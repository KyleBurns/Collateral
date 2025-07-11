package com.example.collateral;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CollateralController {

    private final CollateralService collateralService;

    public CollateralController(CollateralService service) {
        this.collateralService = service;
    }

    /**
     * Endpoint to retrieve collateral values for given account IDs.
     *
     * @param accountIds List of account IDs in request body
     * @return List of collateral responses for each account
     */
    @PostMapping("/collateral")
    public List<CollateralResponse> getCollateral(@RequestBody CollateralRequest request) {
        System.out.println("Received accounts: " + request.accountIds());
        return collateralService.calculateCollateral(request.accountIds());
    }
}