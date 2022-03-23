package org.dumbo.engine.promotion;

import org.dumbo.engine.product.IProduct;

import java.util.Map;

public class MultipleProductPromotion extends AbstractPromotion {

    private Map<Integer, Integer> productsInPromotion; // Key: productId, value: count of product corresponding to productId
    private double promotionAmount;

    public MultipleProductPromotion(Map<Integer, Integer> productsInPromotion, double promotionAmount, int promotionPriority) {
        super(promotionPriority);
        this.productsInPromotion = productsInPromotion;
        this.promotionAmount = promotionAmount;
    }

    public double apply(Map<IProduct, Long> productsInCart) {
        int times = 1000;
        double subAmount = 0.0;

        for (Map.Entry<IProduct, Long> product : productsInCart.entrySet()) {
            for (Map.Entry<Integer, Integer> promotion : this.productsInPromotion.entrySet()) {
                if (product.getKey().getId() == promotion.getKey()) {
                    int withPromotion = (int) (product.getValue() / promotion.getValue());

                    if (withPromotion < times) {
                        times = withPromotion;
                    }

                    product.getKey().setPromotionApplied(true);
                }
            }
        }

        for (Map.Entry<IProduct, Long> product : productsInCart.entrySet()) {
            for (Map.Entry<Integer, Integer> promotion : this.productsInPromotion.entrySet()) {
                if (product.getKey().getId() == promotion.getKey()) {
                    subAmount += (product.getValue() - (times * promotion.getValue())) * product.getKey().getUnitPrice();
                }
            }
        }

        return times * this.promotionAmount + subAmount;
    }

    public boolean isApplicable(Map<IProduct, Long> productsInCart) {
        if (productsInCart == null || productsInCart.size() == 0) {
            throw new RuntimeException("products can not be null or empty!");
        }

        int counter = 0;
        for (Map.Entry<IProduct, Long> product : productsInCart.entrySet()) {
            for (Map.Entry<Integer, Integer> promotion : this.productsInPromotion.entrySet()) {
                if (isValid(promotion, product)) {
                    counter++;
                }
            }
        }

        //Check if all promotion requirements are met by products in the cart
        if (counter == this.productsInPromotion.size()) {
            return true;
        }
        return false;
    }

    private boolean isValid(Map.Entry<Integer, Integer> promotion, Map.Entry<IProduct, Long> product) {
        return product.getKey().getId() == promotion.getKey() && !product.getKey().isPromotionApplied() && product.getValue() >= promotion.getValue();
    }
}
