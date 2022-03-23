package org.dumbo.engine.product;

public class ProductC extends AbstractProduct {

    private static final int ID = 3;
    private static final double UNIT_PRICE = 20d;

    public int getId() {
        return ID;
    }

    public double getUnitPrice() {
        return UNIT_PRICE;
    }
}
