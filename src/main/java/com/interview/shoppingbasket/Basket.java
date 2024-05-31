package com.interview.shoppingbasket;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder(toBuilder = true, setterPrefix = "with")
public class Basket {

    @Builder.Default private List<BasketItem> items = new ArrayList<>();

    public void add(String productCode, String productName, int quantity) {
        BasketItem basketItem = BasketItem.builder()
                .withProductCode(productCode)
                .withProductName(productName)
                .withQuantity(quantity).build();
        items.add(basketItem);
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public void consolidateItems() {
        items = new ArrayList<>(items.stream()
                .collect(Collectors.toMap(
                        BasketItem::getProductCode,
                        item -> item,
                        (item1, item2) -> {
                            item1.setQuantity(item1.getQuantity() + item2.getQuantity());
                            return item1;
                        }
                ))
                .values());
    }
}
