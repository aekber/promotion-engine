package org.dumbo.engine.promotion;

import org.dumbo.engine.product.IProduct;

import java.util.Map;

public class MoreThanOneUnitPromotion implements IPromotion {

    private IProduct product;
    private int unit;
    private double promotionAmount;

    public MoreThanOneUnitPromotion(IProduct product, int unit, double promotionAmount){
        this.product = product;
        this.unit = unit;
        this.promotionAmount = promotionAmount;
    }

    @Override
    public void apply(Map<IProduct, Long> products) {
        if(products == null){
            throw new RuntimeException("products can not be null!");
        }

        products.forEach((key, value) -> {
            if(key.getId() == this.product.getId()){
                int withPromotion = (int) (value / this.unit);
                int withoutPromotion = (int) (value % this.unit);

                double totalAmountWithPromotion = withPromotion * this.promotionAmount + withoutPromotion * key.getUnitPrice();
            }
        });
    }
}
