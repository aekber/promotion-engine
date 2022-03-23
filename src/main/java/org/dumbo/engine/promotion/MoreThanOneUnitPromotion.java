package org.dumbo.engine.promotion;

import org.dumbo.engine.product.IProduct;

import java.util.Map;

public class MoreThanOneUnitPromotion extends AbstractPromotion {

    private int productId;
    private int unit;
    private double promotionAmount;

    public MoreThanOneUnitPromotion(int productId, int unit, double promotionAmount, int promotionPriority) {
        super(promotionPriority);
        this.productId = productId;
        this.unit = unit;
        this.promotionAmount = promotionAmount;
    }

    public double apply(Map<IProduct, Long> products) {
        if (products == null || products.size() == 0) {
            throw new RuntimeException("products can not be null or empty!");
        }

        return products.entrySet().stream()
                                  .filter(this::isPromotionApplicable)
                                  .mapToDouble(product -> {
                                      product.getKey().setPromotionApplied(true);

                                      int withPromotion = (int) (product.getValue() / this.unit);
                                      int withoutPromotion = (int) (product.getValue() % this.unit);

                                      return withPromotion * this.promotionAmount + withoutPromotion * product.getKey().getUnitPrice();

                                  }).sum();
    }

    private boolean isPromotionApplicable(Map.Entry<IProduct, Long> product) {
        return product.getKey().getId() == this.productId && !product.getKey().isPromotionApplied() && product.getValue() >= this.unit;
    }
}
