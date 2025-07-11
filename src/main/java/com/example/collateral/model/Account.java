package com.example.collateral;

import java.util.ArrayList;
import java.util.List;

class Account {

    private final String accountId;
    private final List<AccountPosition> positions;

    public Account(String accountId, List<AccountPosition> positions) {
        this.accountId = accountId;
        this.positions = positions;
    }

    public double getCollateralValue(){
        double total = 0;
        for(AccountPosition position : positions) {
            total += position.getCollateralValue();
        }
        return total;
    }
}