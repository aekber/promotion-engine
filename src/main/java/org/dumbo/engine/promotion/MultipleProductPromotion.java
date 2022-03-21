package org.dumbo.engine.promotion;

import org.dumbo.engine.product.IProduct;

import java.util.Map;

public class MultipleProductPromotion implements IPromotion {

    private Map<Integer, Integer> productsInPromotion; // Key: productId, value: count of product corresponding to productId
    private double promotionAmount;

    public MultipleProductPromotion(Map<Integer, Integer> productsInPromotion, double promotionAmount){
        this.productsInPromotion = productsInPromotion;
        this.promotionAmount = promotionAmount;
    }

    public double apply(Map<IProduct, Long> productsInCart) {
        if(productsInCart == null){
            throw new RuntimeException("products can not be null!");
        }

        int counter = 0;
        for (Map.Entry<Integer, Integer> promotion : this.productsInPromotion.entrySet()) {
            for (Map.Entry<IProduct, Long> product : productsInCart.entrySet()) {
                if(isPromotionApplicable(promotion, product)){
                    counter++;
                }
            }
        }

        if(counter == this.productsInPromotion.size()){
            int times = 1000;
            double subAmount = 0.0;

            for (Map.Entry<Integer, Integer> promotion : this.productsInPromotion.entrySet()) {
                int productIdInPromotion = promotion.getKey();
                int requiredProductCountForPromotion = promotion.getValue();
                for (Map.Entry<IProduct, Long> product : productsInCart.entrySet()) {
                    IProduct product1 = product.getKey();
                    long productCountInCart = product.getValue();
                    if(product1.getId() == productIdInPromotion){
                        int withPromotion = (int) (productCountInCart / requiredProductCountForPromotion);
                        int withoutPromotion = (int) (productCountInCart % requiredProductCountForPromotion);

                        if(withoutPromotion == 0){

                        }

                        subAmount += withoutPromotion * product1.getUnitPrice();
                        if(withPromotion < times){
                            times = withPromotion;
                        }
                        product.getKey().setPromotionApplied(true);
                    }
                }
            }
            return times * this.promotionAmount + subAmount;
        }
        return 0d;
    }

    private boolean isPromotionApplicable(Map.Entry<Integer, Integer> promotion, Map.Entry<IProduct, Long> product) {
        return product.getKey().getId() == promotion.getKey() && !product.getKey().isPromotionApplied() && product.getValue() >= promotion.getValue();
    }

}
