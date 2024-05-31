package com.interview.shoppingbasket;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PromotionCheckoutStep implements CheckoutStep {

    private PromotionsService promotionsService;

    @Override
    public void execute(CheckoutContext checkoutContext) {
        Basket basket = checkoutContext.getBasket();
        List<Promotion> promotionList = promotionsService.getPromotions(basket);
        checkoutContext.setPromotionList(promotionList);
    }
}
