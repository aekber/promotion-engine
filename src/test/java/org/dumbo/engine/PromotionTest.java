package org.dumbo.engine;

import org.dumbo.engine.product.IProduct;
import org.dumbo.engine.product.ProductA;
import org.dumbo.engine.promotion.IPromotion;
import org.dumbo.engine.promotion.MoreThanOneUnitPromotion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class PromotionTest {

    @Before
    public void setUp() {
        System.out.println("Running setUp...");
    }

    @Test
    public void moreThanOneUnitPromotionTest() {
        IProduct product = new ProductA();
        IPromotion promotion = new MoreThanOneUnitPromotion(product, 3, 130d);

        Map<IProduct, Long> map = new HashMap<>();
        map.put(product, 7l);

        promotion.apply(map);
        assertTrue(true);
    }

    @After
    public void tearDown() {
        System.out.println("Running tearDown...");
    }
}

