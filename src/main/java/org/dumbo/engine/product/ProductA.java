package org.dumbo.engine.product;

public class ProductA extends AbstractProduct {

    private static final int ID = 1;
    private static final double UNIT_PRICE = 50d;

    public int getId() {
        return ID;
    }

    public double getUnitPrice() {
        return UNIT_PRICE;
    }
}
