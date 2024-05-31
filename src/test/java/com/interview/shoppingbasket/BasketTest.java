package com.interview.shoppingbasket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class BasketTest {
    @Test
    void emptyBasket() {
        Basket basket = Basket.builder().build();
        List<BasketItem> basketSize = basket.getItems();

        assertEquals(basketSize.size(), 0);
    }

    @Test
    void createBasketFullConstructor() {
        Basket basket = Basket.builder().build();
        basket.add("productCode", "myProduct", 10);
        List<BasketItem> basketSize = basket.getItems();

        assertEquals(1, basketSize.size());
        assertEquals("productCode", basketSize.get(0).getProductCode());
        assertEquals("myProduct", basketSize.get(0).getProductName());
        assertEquals(10, basketSize.get(0).getQuantity());
    }

    @Test
    void createBasketWithMultipleProducts() {
        Basket basket = Basket.builder().build();
        basket.add("productCode", "myProduct", 10);
        basket.add("productCode2", "myProduct2", 10);
        basket.add("productCode3", "myProduct3", 10);

        List<BasketItem> basketItems = basket.getItems();

        assertEquals(3, basketItems.size());

        assertEquals(BasketItem.builder().withProductCode("productCode").withProductName("myProduct").withQuantity(10).build(), basketItems.get(0));
        assertEquals(BasketItem.builder().withProductCode("productCode2").withProductName("myProduct2").withQuantity(10).build(), basketItems.get(1));
        assertEquals(BasketItem.builder().withProductCode("productCode3").withProductName("myProduct3").withQuantity(10).build(), basketItems.get(2));
    }

    static Stream<Arguments> consolidationTestScenarios() {

        ///////////////////////////////////////////////////
        // Test Scenario 1 - No products to consolidate
        ///////////////////////////////////////////////////

        Basket basketScenario1 = Basket.builder().withItems(
                List.of(
                        BasketItem.builder().withProductCode("COMPUTER-MAC").withProductName("Computer Mac").withQuantity(1).build(),
                        BasketItem.builder().withProductCode("GAME-ANTBUSTER").withProductName("PC Game Ant Buster").withQuantity(1).build()
                )
        ).build();
        Basket expectedBasketScenario1 = Basket.builder().withItems(
                List.of(
                        BasketItem.builder().withProductCode("COMPUTER-MAC").withProductName("Computer Mac").withQuantity(1).build(),
                        BasketItem.builder().withProductCode("GAME-ANTBUSTER").withProductName("PC Game Ant Buster").withQuantity(1).build()
                )
        ).build();

        ///////////////////////////////////////////////////
        // Test Scenario 2 - One product code to consolidate
        ///////////////////////////////////////////////////

        Basket basketScenario2 = Basket.builder().withItems(
                List.of(
                        BasketItem.builder().withProductCode("COMPUTER-MAC").withProductName("Computer Mac").withQuantity(1).build(),
                        BasketItem.builder().withProductCode("COMPUTER-MAC").withProductName("Computer Mac").withQuantity(1).build(),
                        BasketItem.builder().withProductCode("GAME-GTA").withProductName("PC Game Ant Buster").withQuantity(1).build()
                )
        ).build();
        Basket expectedBasketScenario2 = Basket.builder().withItems(
                List.of(
                        BasketItem.builder().withProductCode("COMPUTER-MAC").withProductName("Computer Mac").withQuantity(2).build(),
                        BasketItem.builder().withProductCode("GAME-GTA").withProductName("PC Game Ant Buster").withQuantity(1).build()
                )
        ).build();

        ///////////////////////////////////////////////////
        // Test Scenario 3 - Multiple product codes to consolidate
        ///////////////////////////////////////////////////

        Basket basketScenario3 = Basket.builder().withItems(
                List.of(
                        BasketItem.builder().withProductCode("COMPUTER-MAC").withProductName("Computer Mac").withQuantity(1).build(),
                        BasketItem.builder().withProductCode("GAME-GTA").withProductName("PC Game Ant Buster").withQuantity(2).build(),
                        BasketItem.builder().withProductCode("COMPUTER-MAC").withProductName("Computer Mac").withQuantity(3).build(),
                        BasketItem.builder().withProductCode("GAME-GTA").withProductName("PC Game Ant Buster").withQuantity(4).build()
                )
        ).build();
        Basket expectedBasketScenario3 = Basket.builder().withItems(
                List.of(
                        BasketItem.builder().withProductCode("COMPUTER-MAC").withProductName("Computer Mac").withQuantity(4).build(),
                        BasketItem.builder().withProductCode("GAME-GTA").withProductName("PC Game Ant Buster").withQuantity(6).build()
                )
        ).build();

        return Stream.of(
                Arguments.of(basketScenario1, expectedBasketScenario1),
                Arguments.of(basketScenario2, expectedBasketScenario2),
                Arguments.of(basketScenario3, expectedBasketScenario3)
        );
    }

    @ParameterizedTest
    @MethodSource("consolidationTestScenarios")
    void consolidateBasketTest(final Basket basket, final Basket expectedBasket) {
        basket.consolidateItems();
        assertThat(basket.getItems()).containsExactlyInAnyOrderElementsOf(expectedBasket.getItems());
    }
}
