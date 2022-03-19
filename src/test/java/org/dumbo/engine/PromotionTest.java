package org.dumbo.engine;

import org.dumbo.engine.cart.Cart;
import org.dumbo.engine.product.IProduct;
import org.dumbo.engine.product.ProductA;
import org.dumbo.engine.product.ProductB;
import org.dumbo.engine.product.ProductC;
import org.dumbo.engine.promotion.IPromotion;
import org.dumbo.engine.promotion.MoreThanOneUnitPromotion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @After
    public void tearDown() {
    }
}

