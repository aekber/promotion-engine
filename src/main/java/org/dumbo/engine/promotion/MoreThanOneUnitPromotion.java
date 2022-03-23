package org.dumbo.engine.promotion;

import org.dumbo.engine.product.IProduct;

import java.util.Map;

public class MoreThanOneUnitPromotion extends AbstractPromotion {

    private Map<Integer, Integer> productsInPromotion; // Key: productId, value: count of product corresponding to productId
    private double promotionAmount;

    public MoreThanOneUnitPromotion(Map<Integer, Integer> productsInPromotion, double promotionAmount, int promotionPriority) {
        super(promotionPriority);
        this.productsInPromotion = productsInPromotion;
        this.promotionAmount = promotionAmount;
    }

    public double apply(Map<IProduct, Long> productsInCart) {
        double amount = 0.0;
        for (Map.Entry<IProduct, Long> product : productsInCart.entrySet()) {
            for (Map.Entry<Integer, Integer> promotion : this.productsInPromotion.entrySet()) {
                if (promotion.getKey() == product.getKey().getId()) {
                    int withPromotion = (int) (product.getValue() / promotion.getValue());
                    int withoutPromotion = (int) (product.getValue() % promotion.getValue());

                    amount = withPromotion * this.promotionAmount + withoutPromotion * product.getKey().getUnitPrice();
                    product.getKey().setPromotionApplied(true);
                }
            }
        }
        return amount;
    }

    public boolean isApplicable(Map<IProduct, Long> productsInCart) {
        if (productsInCart == null || productsInCart.size() == 0) {
            throw new RuntimeException("products can not be null or empty!");
        }

        if(productsInPromotion == null || productsInPromotion.size() != 1) {
            throw new RuntimeException("invalid product count in promotion list!");
        }

        for (Map.Entry<IProduct, Long> product : productsInCart.entrySet()) {
            if(isValid(product)){
                return true;
            }
        }
        return false;
    }

    private boolean isValid(Map.Entry<IProduct, Long> product) {
        return product.getKey().getId() == (int) productsInPromotion.keySet().toArray()[0] &&
               !product.getKey().isPromotionApplied() &&
               product.getValue() >= (int) productsInPromotion.values().toArray()[0];
    }
}
