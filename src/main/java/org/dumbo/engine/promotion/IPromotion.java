package org.dumbo.engine.promotion;

import org.dumbo.engine.product.IProduct;

import java.util.Map;

public interface IPromotion {
    void apply(Map<IProduct, Long> products);
}
