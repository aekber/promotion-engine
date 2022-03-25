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
        Map<IProduct, Long> productMap = getProductsAsMap();

        //Total amount of promotioned products
        double amount = this.promotions.stream()
                                       .filter(promotion -> promotion.isApplicable(productMap))
                                       .mapToDouble(promotion -> promotion.apply(productMap))
                                       .sum();

        //Total amount of non-promotioned products
        amount += productMap.entrySet().stream()
                                       .filter(product -> !product.getKey().isPromotionApplied())
                                       .mapToDouble(product -> product.getValue() * product.getKey().getUnitPrice())
                                       .sum();

        return amount;
    }
}
