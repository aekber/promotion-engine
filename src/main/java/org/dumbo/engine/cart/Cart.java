package org.dumbo.engine.cart;

import org.dumbo.engine.product.*;
import org.dumbo.engine.promotion.IPromotion;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Cart {

    private List<IPromotion> promotions = new ArrayList<>();
    private List<IProduct> products = new ArrayList<>();

    //Promotion with higher priority is processed first
    public void setPromotions(List<IPromotion> promotions) {
        List<IPromotion> sortedByPriorityList = promotions.stream()
                                                          .sorted(Comparator.comparing(IPromotion::getPriority).reversed())
                                                          .collect(Collectors.toList());
        this.promotions = sortedByPriorityList;
    }

    public void addItem(IProduct product) {
        if(product == null) {
            throw new RuntimeException("product can not be null");
        }
        products.add(product);
    }

    // Returns product map sorted by value that corresponds to number of each product
    private Map<IProduct, Long> getProductsAsMap() {
        return products.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    //Returns new amount of cart after applying promotions
    public double getPromotionsAppliedTotalAmount() {
        double amount = 0.0;
        Map<IProduct, Long> productMap = getProductsAsMap();

        //Calculates amount of promotioned products
        for(IPromotion promotion : this.promotions) {
            if(promotion.isApplicable(productMap)) {
                amount += promotion.apply(productMap);
            }
        }

        //Calculates amount of non-promotioned products
        for (Map.Entry<IProduct, Long> entry : productMap.entrySet()) {
            if(!entry.getKey().isPromotionApplied()){
                amount += entry.getValue() * entry.getKey().getUnitPrice();
            }
        }
        return amount;
    }
}
