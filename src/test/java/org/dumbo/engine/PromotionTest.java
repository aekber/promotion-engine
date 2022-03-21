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

    @Before
    public void setUp() {
    }

    @Test
    public void moreThanOneUnitPromotionTest() {
        List<IPromotion> promotions = new ArrayList<>();
        promotions.add(new MoreThanOneUnitPromotion(1, 3, 130));
        promotions.add(new MoreThanOneUnitPromotion(2, 2, 50));

        Cart c = new Cart();
        c.addItem(new ProductA());
        c.addItem(new ProductB());
        c.addItem(new ProductA());
        c.addItem(new ProductA());
        c.addItem(new ProductB());
        c.addItem(new ProductB());
        c.addItem(new ProductC());
        c.addItem(new ProductA());
        c.addItem(new ProductA());

        c.setPromotions(promotions);

        assertEquals(c.applyPromotions(), 325.0, 0.001);
    }

    @Test
    public void multipleProductPromotionTest() {
        List<IPromotion> promotions = new ArrayList<>();
        Map<Integer, Integer> promotionProducts = new HashMap<>();
        promotionProducts.put(3, 1);
        promotionProducts.put(4, 1);
        promotions.add(new MultipleProductPromotion(promotionProducts,20d));

        Cart c = new Cart();
        c.addItem(new ProductA());
        c.addItem(new ProductD());
        c.addItem(new ProductC());
        c.addItem(new ProductB());

        c.setPromotions(promotions);

        assertEquals(c.applyPromotions(), 100.0, 0.001);
    }

    //3A=130
    //2B=50
    //C+D=20
    @Test
    public void moreThanOneUnitPromotionTest3() {
        Map<Integer, Integer> promotionProducts = new HashMap<>();
        promotionProducts.put(3, 1);
        promotionProducts.put(4, 1);

        List<IPromotion> promotions = new ArrayList<>();
        promotions.add(new MoreThanOneUnitPromotion(1, 3, 130));
        promotions.add(new MoreThanOneUnitPromotion(2, 2, 50));
        promotions.add(new MultipleProductPromotion(promotionProducts,20d));

        Cart c = new Cart();
        c.addItem(new ProductA());
        c.addItem(new ProductB());
        c.addItem(new ProductA());
        c.addItem(new ProductA());
        c.addItem(new ProductB());
        c.addItem(new ProductB());
        c.addItem(new ProductC());
        c.addItem(new ProductA());
        c.addItem(new ProductA());
        c.addItem(new ProductD());

        c.setPromotions(promotions);

        assertEquals(c.applyPromotions(), 330.0, 0.001);
    }

    //3A+2B+C=180
    @Test
    public void multipleProductPromotionTest2() {
        List<IPromotion> promotions = new ArrayList<>();
        Map<Integer, Integer> promotionProducts = new HashMap<>();
        promotionProducts.put(1, 3);
        promotionProducts.put(2, 2);
        promotionProducts.put(3, 1);
        promotions.add(new MultipleProductPromotion(promotionProducts,180d));

        //4A+3B+2C+D
        Cart c = new Cart();
        c.addItem(new ProductB());
        c.addItem(new ProductA());
        c.addItem(new ProductD());
        c.addItem(new ProductC());
        c.addItem(new ProductA());
        c.addItem(new ProductB());
        c.addItem(new ProductA());
        c.addItem(new ProductA());
        c.addItem(new ProductB());
        c.addItem(new ProductC());

        c.setPromotions(promotions);

        assertEquals(c.applyPromotions(), 285.0, 0.001);
    }

    @After
    public void tearDown() {
    }
}

