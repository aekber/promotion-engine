package org.dumbo.engine.promotion;

import org.dumbo.engine.product.IProduct;

import java.util.Map;

public interface IPromotion {
    double apply(Map<IProduct, Long> products);
    boolean isApplicable(Map<IProduct, Long> productMap);
    int getPriority();
    double getPromotionAmount();
}
