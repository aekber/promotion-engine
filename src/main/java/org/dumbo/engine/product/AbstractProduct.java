package org.dumbo.engine.product;

public abstract class AbstractProduct implements IProduct {

    private boolean promotionApplied = false;

    public void setPromotionApplied(boolean promotionApplied) {
        this.promotionApplied = promotionApplied;
    }

    public boolean isPromotionApplied() {
        return promotionApplied;
    }

    public int hashCode() {
        return Integer.hashCode(getId());
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof IProduct))
            return false;
        IProduct other = (IProduct)o;
        return this.getId() == other.getId();
    }

    public String toString() {
        return "Product[id: " + getId() + "]";
    }
}
