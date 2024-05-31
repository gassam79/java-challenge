package com.interview.shoppingbasket;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

class CheckoutContext {
    private Basket basket;
    private double retailPriceTotal = 0.0;

    @Getter
    @Setter
    private List<Promotion> promotionList = new ArrayList<>();

    public void setRetailPriceTotal(double retailPriceTotal) {
        this.retailPriceTotal = retailPriceTotal;
    }

    CheckoutContext(Basket basket) {
        this.basket = basket;
    }

    public PaymentSummary paymentSummary() {
        return new PaymentSummary(retailPriceTotal);
    }


    public Basket getBasket() {
        return basket;
    }
}
