package com.interview.shoppingbasket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PromotionCheckoutStepTest {

    @Spy
    @InjectMocks
    PromotionCheckoutStep promotionCheckoutStep;

    @Mock
    PromotionsService promotionsService;

    @Mock
    CheckoutContext checkoutContext;

    @Mock
    Basket basket;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldApplyPromotionsToBasket() {
        Promotion promotion1 = new Promotion(PromotionType.TWO_FOR_ONE, "productCode1");
        Promotion promotion2 = new Promotion(PromotionType.FIFTY_PERCENT_OFF, "productCode2");
        Promotion promotion3 = new Promotion(PromotionType.TEN_PERCENT_OFF, "productCode3");
        List<Promotion> promotionList = Arrays.asList(promotion1, promotion2, promotion3);

        when(checkoutContext.getBasket()).thenReturn(basket);
        when(promotionsService.getPromotions(basket)).thenReturn(promotionList);

        promotionCheckoutStep.execute(checkoutContext);

        verify(checkoutContext).setPromotionList(promotionList);
        verify(promotionsService).getPromotions(basket);
    }
}
