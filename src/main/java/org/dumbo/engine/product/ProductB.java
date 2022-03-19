package org.dumbo.engine.product;

public class ProductB extends AbstractProduct {

    private static final int ID = 2;
    private static final double UNIT_PRICE = 30d;

    public int getId() {
        return ID;
    }

    public double getUnitPrice() {
        return UNIT_PRICE;
    }
}
