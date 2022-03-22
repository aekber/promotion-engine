package org.dumbo.engine.cart;

import org.dumbo.engine.product.*;
import org.dumbo.engine.promotion.IPromotion;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

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
        Map<IProduct, Long> productMap = products.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        return productMap.entrySet().stream()
                                    .sorted(comparingByValue())
                                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }

    //Returns new amount of cart after applying promotions
    public double getPromotionsAppliedTotalAmount() {
        double amount = 0.0;
        Map<IProduct, Long> productMap = getProductsAsMap();
        for(IPromotion promotion : this.promotions) {
            amount += promotion.apply(productMap);
        }

        for (Map.Entry<IProduct, Long> entry : productMap.entrySet()) {
            if(!entry.getKey().isPromotionApplied()){
                amount += entry.getValue() * entry.getKey().getUnitPrice();
            }
        }
        return amount;
    }
}
