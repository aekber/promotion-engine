package org.dumbo.engine.promotion;

import org.dumbo.engine.product.IProduct;

import java.util.Map;

public class MoreThanOneUnitPromotion extends AbstractPromotion {

    private int productId;
    private int unit;
    private double promotionAmount;

    public MoreThanOneUnitPromotion(int productId, int unit, double promotionAmount, int promotionPriority){
        super(promotionPriority);
        this.productId = productId;
        this.unit = unit;
        this.promotionAmount = promotionAmount;
    }

    public double apply(Map<IProduct, Long> products) {
        if(products == null){
            throw new RuntimeException("products can not be null!");
        }

        double amount = 0.0;
        for (Map.Entry<IProduct, Long> product : products.entrySet()) {
            if(isPromotionApplicable(product)){
                int withPromotion = (int) (product.getValue() / this.unit);
                int withoutPromotion = (int) (product.getValue() % this.unit);

                amount = withPromotion * this.promotionAmount + withoutPromotion * product.getKey().getUnitPrice();
                product.getKey().setPromotionApplied(true);
            }
        }
        return amount;
    }

    private boolean isPromotionApplicable(Map.Entry<IProduct, Long> product) {
        return product.getKey().getId() == this.productId && !product.getKey().isPromotionApplied() && product.getValue() >= this.unit;
    }
}
