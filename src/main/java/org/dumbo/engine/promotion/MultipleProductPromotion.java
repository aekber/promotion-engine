package org.dumbo.engine.promotion;

import org.dumbo.engine.product.IProduct;

import java.util.Map;

public class MultipleProductPromotion extends AbstractPromotion {

    public MultipleProductPromotion(PromotionAggregator aggregator) {
        super(aggregator);
    }

    public double apply(Map<IProduct, Long> productsInCart) {
        int times = 1000; //This variable holds that how many times promotion can applied
        for (Map.Entry<IProduct, Long> product : productsInCart.entrySet()) { // Key: product, value: count of product corresponding to productId
            for (Map.Entry<Integer, Integer> promotion : getProductsInPromotion().entrySet()) { // Key: productId, value: required count of product for promotion
                if (product.getKey().getId() == promotion.getKey()) {
                    int promotionFactor = (int) (product.getValue() / promotion.getValue());

                    if (promotionFactor < times) {
                        times = promotionFactor;
                    }

                    product.getKey().setPromotionApplied(true);
                }
            }
        }

        double subAmount = 0.0; //This variable holds amount of residual products after promotion applied
        for (Map.Entry<IProduct, Long> product : productsInCart.entrySet()) { // Key: product, value: count of product corresponding to productId
            for (Map.Entry<Integer, Integer> promotion : getProductsInPromotion().entrySet()) { // Key: productId, value: required count of product for promotion
                if (product.getKey().getId() == promotion.getKey()) {
                    subAmount += (product.getValue() - (times * promotion.getValue())) * product.getKey().getUnitPrice();
                }
            }
        }

        return times * getPromotionAmount() + subAmount;
    }

    public boolean isApplicable(Map<IProduct, Long> productsInCart) {
        if (productsInCart == null || productsInCart.size() == 0) {
            throw new RuntimeException("products can not be null or empty!");
        }

        int counter = 0;
        for (Map.Entry<IProduct, Long> product : productsInCart.entrySet()) {
            for (Map.Entry<Integer, Integer> promotion : getProductsInPromotion().entrySet()) {
                if (isValid(promotion, product)) {
                    counter++;
                }
            }
        }

        //Check if all promotion requirements are met by products in the cart
        return counter == getProductsInPromotion().size();
    }

    private boolean isValid(Map.Entry<Integer, Integer> promotion, Map.Entry<IProduct, Long> product) {
        return product.getKey().getId() == promotion.getKey() &&
               !product.getKey().isPromotionApplied() &&
               product.getValue() >= promotion.getValue();
    }
}
