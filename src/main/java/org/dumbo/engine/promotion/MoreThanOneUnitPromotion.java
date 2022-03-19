package org.dumbo.engine.promotion;

import org.dumbo.engine.product.IProduct;

import java.util.Map;

public class MoreThanOneUnitPromotion implements IPromotion {

    private int productId;
    private int unit;
    private double promotionAmount;

    public MoreThanOneUnitPromotion(int productId, int unit, double promotionAmount){
        this.productId = productId;
        this.unit = unit;
        this.promotionAmount = promotionAmount;
    }

    public double apply(Map<IProduct, Long> products) {
        if(products == null){
            throw new RuntimeException("products can not be null!");
        }

        double amount = 0.0;
        for (Map.Entry<IProduct, Long> entry : products.entrySet()) {
            IProduct product = entry.getKey();
            if(product.getId() == this.productId && !product.isPromotionApplied()){
                Long productCount = entry.getValue();
                int withPromotion = (int) (productCount / this.unit);
                int withoutPromotion = (int) (productCount % this.unit);

                amount = withPromotion * this.promotionAmount + withoutPromotion * product.getUnitPrice();

                product.setPromotionApplied(true);
            }
        }
        return amount;
    }
}
