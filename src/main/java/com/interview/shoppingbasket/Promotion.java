package com.interview.shoppingbasket;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true, setterPrefix = "with")
public class Promotion {
    private PromotionType type;
    private String productCode;
}
