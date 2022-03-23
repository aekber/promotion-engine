package org.dumbo.engine.promotion;


import org.dumbo.engine.product.IProduct;

import java.util.Map;

public abstract class AbstractPromotion implements IPromotion{

    private int priority;

    public AbstractPromotion(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
