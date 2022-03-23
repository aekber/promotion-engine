package org.dumbo.engine.product;

public class ProductD extends AbstractProduct {

    private static final int ID = 4;
    private static final double UNIT_PRICE = 15d;

    public int getId() {
        return ID;
    }

    public double getUnitPrice() {
        return UNIT_PRICE;
    }
}
