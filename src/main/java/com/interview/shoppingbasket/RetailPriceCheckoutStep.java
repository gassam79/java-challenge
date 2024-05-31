package com.interview.shoppingbasket;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RetailPriceCheckoutStep implements CheckoutStep {
    private PricingService pricingService;
    private double retailTotal;

    public RetailPriceCheckoutStep(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Override
    public void execute(CheckoutContext checkoutContext) {
        Basket basket = checkoutContext.getBasket();
        List<Promotion> promotionList = checkoutContext.getPromotionList();
        Map<String, Promotion> promotionByProduct = promotionList.stream().collect(Collectors.toMap(Promotion::getProductCode, promotion -> promotion));

        retailTotal = 0.0;

        for (BasketItem basketItem: basket.getItems()) {
            double price = pricingService.getPrice(basketItem.getProductCode());
            basketItem.setProductRetailPrice(price);
            double itemPriceAfterPromotion = applyPromotion(promotionByProduct.get(basketItem.getProductCode()), basketItem);
            retailTotal += itemPriceAfterPromotion;
        }

        checkoutContext.setRetailPriceTotal(retailTotal);
    }

    public double applyPromotion(Promotion promotion, BasketItem item) {
        if (promotion != null) {
            return promotion.getType().applyPromotion(item.getQuantity(), item.getProductRetailPrice());
        }
        return item.getItemPrice();
    }
}
