package org.dumbo.engine.cart;

import org.dumbo.engine.product.IProduct;
import org.dumbo.engine.promotion.IPromotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Cart {

    private List<IPromotion> promotions = new ArrayList<>();
    private List<IProduct> products = new ArrayList<>();

    public void setPromotions(List<IPromotion> promotions) {
        this.promotions = promotions;
    }

    public void addItem(IProduct product) {
        if(product == null) {
            throw new RuntimeException("product can not be null");
        }
        products.add(product);
    }

    // Returns product map that corresponds to number of each product
    private Map<IProduct, Long> getProductMap() {
        return products.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public void applyPromotions(){
        this.promotions.forEach((promotion) -> promotion.apply(getProductMap()));
    }
}
