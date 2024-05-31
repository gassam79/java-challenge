package com.interview.shoppingbasket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CheckoutPipelineE2ETest {

    @InjectMocks
    CheckoutPipeline checkoutPipeline;

    @Mock
    PromotionsService promotionsService;

    @Mock
    PricingService pricingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        checkoutPipeline.addStep(new BasketConsolidationCheckoutStep());
        checkoutPipeline.addStep(new PromotionCheckoutStep(promotionsService));
        checkoutPipeline.addStep(new RetailPriceCheckoutStep(pricingService));

        // Setup product prices
        when(pricingService.getPrice("productCode1")).thenReturn(10.0);
        when(pricingService.getPrice("productCode2")).thenReturn(15.0);
        when(pricingService.getPrice("productCode3")).thenReturn(20.0);
    }

    static Stream<Arguments> e2eTestScenarios() {

        // No products or promotions - should return 0.0
        Arguments scenario1 = Arguments.of(
                Collections.emptyList(),
                Collections.emptyList(),
                0.0
        );

        // Products already consolidated - No Promotions - Should return the direct quantity * price sum
        Arguments scenario2 = Arguments.of(
                List.of(
                        BasketItem.builder().withProductCode("productCode1").withProductName("Product 1").withQuantity(1).build(),
                        BasketItem.builder().withProductCode("productCode2").withProductName("Product 2").withQuantity(1).build()
                ),
                Collections.emptyList(),
                25.0
        );

        // Products not consolidated - No Promotions - Should return the price sum
        Arguments scenario3 = Arguments.of(
                List.of(
                        BasketItem.builder().withProductCode("productCode1").withProductName("Product 1").withQuantity(1).build(),
                        BasketItem.builder().withProductCode("productCode2").withProductName("Product 2").withQuantity(1).build(),
                        BasketItem.builder().withProductCode("productCode1").withProductName("Product 1").withQuantity(2).build(),
                        BasketItem.builder().withProductCode("productCode1").withProductName("Product 1").withQuantity(4).build()
                ),
                Collections.emptyList(),
                85.0
        );

        // Products not consolidated - 1 Promotion
        Arguments scenario4 = Arguments.of(
                List.of(
                        BasketItem.builder().withProductCode("productCode1").withProductName("Product 1").withQuantity(1).build(),
                        BasketItem.builder().withProductCode("productCode2").withProductName("Product 2").withQuantity(1).build(),
                        BasketItem.builder().withProductCode("productCode1").withProductName("Product 1").withQuantity(2).build(),
                        BasketItem.builder().withProductCode("productCode1").withProductName("Product 1").withQuantity(4).build()
                ),
                List.of(
                        Promotion.builder().withType(PromotionType.TWO_FOR_ONE).withProductCode("productCode1").build()
                ),
                55.0
        );

        // Products not consolidated - Multiple Promotions
        Arguments scenario5 = Arguments.of(
                List.of(
                        BasketItem.builder().withProductCode("productCode1").withProductName("Product 1").withQuantity(1).build(),
                        BasketItem.builder().withProductCode("productCode2").withProductName("Product 2").withQuantity(1).build(),
                        BasketItem.builder().withProductCode("productCode1").withProductName("Product 1").withQuantity(2).build(),
                        BasketItem.builder().withProductCode("productCode1").withProductName("Product 1").withQuantity(4).build(),
                        BasketItem.builder().withProductCode("productCode3").withProductName("Product 3").withQuantity(2).build()
                ),
                List.of(
                        Promotion.builder().withType(PromotionType.TWO_FOR_ONE).withProductCode("productCode1").build(),
                        Promotion.builder().withType(PromotionType.FIFTY_PERCENT_OFF).withProductCode("productCode2").build(),
                        Promotion.builder().withType(PromotionType.TEN_PERCENT_OFF).withProductCode("productCode3").build()
                ),
                83.5
        );

        return Stream.of(scenario1, scenario2, scenario3, scenario4, scenario5);
    }

    @ParameterizedTest
    @MethodSource("e2eTestScenarios")
    void testCheckoutPipeline(final List<BasketItem> basketItems, final List<Promotion> promotions, final double expectedPrice) {

        Basket basket = Basket.builder().withItems(basketItems).build();
        when(promotionsService.getPromotions(basket)).thenReturn(promotions);
        PaymentSummary paymentSummary = checkoutPipeline.checkout(basket);
        assertEquals(expectedPrice, paymentSummary.getRetailTotal());
    }

}
