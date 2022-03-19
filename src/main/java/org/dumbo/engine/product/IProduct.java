package org.dumbo.engine.product;

public interface IProduct {
    int getId();
    double getUnitPrice();
    void setPromotionApplied(boolean promotionApplied);
    boolean isPromotionApplied();
}
