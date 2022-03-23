package org.dumbo.engine.promotion;

import org.dumbo.engine.product.IProduct;

import java.util.Map;

public interface IPromotion {
    double apply(Map<IProduct, Long> products);
    int getPriority();
    boolean isApplicable(Map<IProduct, Long> productMap);
}
