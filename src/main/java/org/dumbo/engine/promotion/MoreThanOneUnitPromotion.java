package org.dumbo.engine.promotion;

import org.dumbo.engine.product.IProduct;

import java.util.Map;

public class MoreThanOneUnitPromotion extends AbstractPromotion {

    public MoreThanOneUnitPromotion(PromotionAggregator aggregator) {
        super(aggregator);
    }

    public double apply(Map<IProduct, Long> productsInCart) {
        double amount = 0.0;
        for (Map.Entry<IProduct, Long> product : productsInCart.entrySet()) {
            for (Map.Entry<Integer, Integer> promotion : getProductsInPromotion().entrySet()) {
                if (promotion.getKey() == product.getKey().getId()) {
                    int withPromotion = (int) (product.getValue() / promotion.getValue());
                    int withoutPromotion = (int) (product.getValue() % promotion.getValue());

                    amount = withPromotion * getPromotionAmount() + withoutPromotion * product.getKey().getUnitPrice();
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

        if(getProductsInPromotion() == null || getProductsInPromotion().size() != 1) {
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
        return product.getKey().getId() == (int) getProductsInPromotion().keySet().toArray()[0] &&
               !product.getKey().isPromotionApplied() &&
               product.getValue() >= (int) getProductsInPromotion().values().toArray()[0];
    }
}
