package org.dumbo.engine.cart;

import org.dumbo.engine.product.*;
import org.dumbo.engine.promotion.IPromotion;
import org.dumbo.engine.promotion.MoreThanOneUnitPromotion;

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
    private Map<IProduct, Long> getProductsAsMap() {
        return products.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    //Returns new amount of cart after applying promotions
    public double applyPromotions() {
        double amount = 0.0;
        Map<IProduct, Long> productMap = getProductsAsMap();
        for(IPromotion promotion : this.promotions) {
            amount += promotion.apply(productMap);
        }

        for (Map.Entry<IProduct, Long> entry : productMap.entrySet()) {
            if(!entry.getKey().isPromotionApplied()){
                amount += entry.getKey().getUnitPrice();
            }
        }
        return amount;
    }
}
