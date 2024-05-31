package com.interview.shoppingbasket;

enum PromotionType {
    TWO_FOR_ONE {
        @Override
        public double applyPromotion(Integer quantity, double retailPrice) {
            int payableQuantity = quantity / 2 + quantity % 2;
            return payableQuantity * retailPrice;
        }
    },
    FIFTY_PERCENT_OFF {
        @Override
        public double applyPromotion(Integer quantity, double retailPrice) {
            return quantity * retailPrice * 0.5;
        }
    },
    TEN_PERCENT_OFF {
        @Override
        public double applyPromotion(Integer quantity, double retailPrice) {
            return quantity * retailPrice * 0.9;
        }
    };

    public abstract double applyPromotion(Integer quantity, double retailPrice);
}