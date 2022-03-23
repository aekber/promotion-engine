package org.dumbo.engine.promotion;

import java.util.Map;

public abstract class AbstractPromotion implements IPromotion{

    private PromotionAggregator aggregator;

    public AbstractPromotion(PromotionAggregator aggregator) {
        this.aggregator = aggregator;
    }

    public int getPriority() {
        return this.aggregator.priority;
    }

    public double getPromotionAmount() {
        return this.aggregator.promotionAmount;
    }

    public Map<Integer, Integer> getProductsInPromotion() {
        return this.aggregator.productsInPromotion;
    }

    public static class PromotionAggregator {
        public Map<Integer, Integer> productsInPromotion; // Key: productId, value: count of product corresponding to productId
        public double promotionAmount;
        public int priority;
    }
}
