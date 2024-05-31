package com.interview.shoppingbasket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "with")
public class BasketItem {
    private String productCode;
    private String productName;
    private int quantity;
    private double productRetailPrice;

    public double getItemPrice() {
        return quantity * productRetailPrice;
    }
}
