package com.interview.shoppingbasket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CheckoutPipelineTest {

    CheckoutPipeline checkoutPipeline;

    @Mock
    Basket basket;

    @Mock
    BasketConsolidationCheckoutStep basketConsolidationCheckoutStep;
    @Mock
    PromotionCheckoutStep promotionCheckoutStep;
    @Mock
    RetailPriceCheckoutStep retailPriceCheckoutStep;

    @BeforeEach
    void setup() {
        checkoutPipeline = new CheckoutPipeline();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void returnZeroPaymentForEmptyPipeline() {
        PaymentSummary paymentSummary = checkoutPipeline.checkout(basket);

        assertEquals(paymentSummary.getRetailTotal(), 0.0);
    }

    @Test
    void executeAllPassedCheckoutSteps() {

        checkoutPipeline.addStep(basketConsolidationCheckoutStep);
        checkoutPipeline.addStep(promotionCheckoutStep);
        checkoutPipeline.addStep(retailPriceCheckoutStep);

        checkoutPipeline.checkout(basket);

        verify(basketConsolidationCheckoutStep, times(1)).execute(any(CheckoutContext.class));
        verify(promotionCheckoutStep, times(1)).execute(any(CheckoutContext.class));
        verify(retailPriceCheckoutStep, times(1)).execute(any(CheckoutContext.class));
    }

}
