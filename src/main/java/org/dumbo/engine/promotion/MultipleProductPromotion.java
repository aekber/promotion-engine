package org.dumbo.engine.promotion;

import org.dumbo.engine.product.IProduct;

import java.util.Map;

public class MultipleProductPromotion implements IPromotion {

    public double apply(Map<IProduct, Long> products) {
        return 0.0;
    }
}
