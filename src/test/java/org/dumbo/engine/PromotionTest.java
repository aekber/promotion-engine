package org.dumbo.engine;

import org.dumbo.engine.cart.Cart;
import org.dumbo.engine.product.*;
import org.dumbo.engine.promotion.IPromotion;
import org.dumbo.engine.promotion.MoreThanOneUnitPromotion;
import org.dumbo.engine.promotion.MultipleProductPromotion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PromotionTest {

    private List<IPromotion> promotions;
    private Cart cart;
    
    @Before
    public void setUp() {
        promotions = new ArrayList<>();
        cart = new Cart();
    }

    @Test(expected = RuntimeException.class)
    public void emptyProductListForMoreThanOneUnitPromotionTest() {
        promotions.add(new MoreThanOneUnitPromotion(1, 3, 130, 1));
        cart.setPromotions(promotions);

        cart.getPromotionsAppliedTotalAmount();
    }

    @Test(expected = RuntimeException.class)
    public void emptyProductListForMultipleProductPromotionTest() {
        promotions.add(new MultipleProductPromotion(null,20d, 1));
        cart.setPromotions(promotions);

        cart.getPromotionsAppliedTotalAmount();
    }

    @Test(expected = RuntimeException.class)
    public void invalidItemInCartTest() {
        promotions.add(new MoreThanOneUnitPromotion(1, 3, 130, 1));
        cart.setPromotions(promotions);

        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(null);
        cart.addItem(new ProductA());

        cart.getPromotionsAppliedTotalAmount();
    }

    //3A=130
    //4B=100
    @Test
    public void moreThanOneUnitPromotionTest1() {
        promotions.add(new MoreThanOneUnitPromotion(1, 3, 130, 1));
        promotions.add(new MoreThanOneUnitPromotion(2, 4, 100, 1));

        //5A+3B+C
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 335.0, 0.001);
    }

    //C+D=20
    @Test
    public void multipleProductPromotionTest1() {
        Map<Integer, Integer> promotionProducts = new HashMap<>();
        promotionProducts.put(3, 1);
        promotionProducts.put(4, 1);

        promotions.add(new MultipleProductPromotion(promotionProducts,20d, 1));

        //A+B+C+D
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductB());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 100.0, 0.001);
    }

    //3A=130
    //2B=50
    //C+D=20
    @Test
    public void mixPromotionsTest1() {
        Map<Integer, Integer> promotionProducts = new HashMap<>();
        promotionProducts.put(3, 1);
        promotionProducts.put(4, 1);

        promotions.add(new MoreThanOneUnitPromotion(1, 3, 130, 1));
        promotions.add(new MoreThanOneUnitPromotion(2, 2, 50, 1));
        promotions.add(new MultipleProductPromotion(promotionProducts,20d, 1));

        //5A+3B+C+D
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 330.0, 0.001);
    }

    //3A+2B+C=180
    @Test
    public void multipleProductPromotionTest2() {
        Map<Integer, Integer> promotionProducts = new HashMap<>();
        promotionProducts.put(1, 3);
        promotionProducts.put(2, 2);
        promotionProducts.put(3, 1);

        promotions.add(new MultipleProductPromotion(promotionProducts,180d, 1));

        //4A+3B+2C+D
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 285.0, 0.001);
    }

    //A+B+3C+4D=210
    @Test
    public void multipleProductPromotionTest3() {
        Map<Integer, Integer> promotionProducts = new HashMap<>();
        promotionProducts.put(1, 1); //A
        promotionProducts.put(2, 1); //2B
        promotionProducts.put(3, 3); //3C
        promotionProducts.put(4, 4); //4D

        promotions.add(new MultipleProductPromotion(promotionProducts,210d, 1));

        //4A+3B+5C+6D
        cart.addItem(new ProductC());
        cart.addItem(new ProductD());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductC());
        cart.addItem(new ProductD());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());
        cart.addItem(new ProductD());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 470, 0.001);
    }

    //3C+4D=75
    //3A=130
    //2B=50
    @Test
    public void mixPromotionsTest2() {
        Map<Integer, Integer> promotionProducts = new HashMap<>();
        promotionProducts.put(3, 3); //3C
        promotionProducts.put(4, 4); //4D

        promotions.add(new MultipleProductPromotion(promotionProducts,75d, 1));
        promotions.add(new MoreThanOneUnitPromotion(1, 3, 130d, 1));
        promotions.add(new MoreThanOneUnitPromotion(2, 2, 50d, 1));

        //4A+3B+5C+6D
        cart.addItem(new ProductC());
        cart.addItem(new ProductD());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductC());
        cart.addItem(new ProductD());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());
        cart.addItem(new ProductD());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 385, 0.001);
    }

    //3A+2B+C=180
    @Test
    public void multipleProductPromotionTest4() {
        Map<Integer, Integer> promotionProducts = new HashMap<>();
        promotionProducts.put(1, 3);
        promotionProducts.put(2, 2);
        promotionProducts.put(3, 1);

        promotions.add(new MultipleProductPromotion(promotionProducts,180d, 1));

        //12A+8B+4C
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 720, 0.001);
    }

    //3A+2B+C=180
    @Test
    public void multipleProductPromotionTest5() {
        Map<Integer, Integer> promotionProducts = new HashMap<>();
        promotionProducts.put(1, 3);
        promotionProducts.put(2, 2);
        promotionProducts.put(3, 1);

        promotions.add(new MultipleProductPromotion(promotionProducts,180d, 1));

        //15A+10B+4C
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 930, 0.001);
    }

    //3A+2B+C=180
    //3A=130
    @Test
    public void mixPromotionsTest3() {
        Map<Integer, Integer> promotionProducts = new HashMap<>();
        promotionProducts.put(1, 3);
        promotionProducts.put(2, 2);
        promotionProducts.put(3, 1);

        promotions.add(new MoreThanOneUnitPromotion(1, 3, 130, 1));
        promotions.add(new MultipleProductPromotion(promotionProducts,180d, 1));

        //4A+3B+2C+D
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 310.0, 0.001);
    }

    @After
    public void tearDown() {
    }
}

