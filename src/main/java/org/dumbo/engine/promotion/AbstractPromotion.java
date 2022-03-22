package org.dumbo.engine.promotion;


public abstract class AbstractPromotion implements IPromotion{

    private int priority;

    public AbstractPromotion(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

}
