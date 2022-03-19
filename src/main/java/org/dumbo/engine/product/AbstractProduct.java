package org.dumbo.engine.product;

public abstract class AbstractProduct implements IProduct {

    public abstract int getId();

    public int hashCode() {
        return new Integer(getId()).hashCode();
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
