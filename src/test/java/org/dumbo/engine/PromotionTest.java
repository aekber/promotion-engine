package org.dumbo.engine;

import org.dumbo.engine.cart.Cart;
import org.dumbo.engine.product.*;
import org.dumbo.engine.promotion.AbstractPromotion.PromotionAggregator;
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
    private Map<Integer, Integer> promotionProducts;

    @Before
    public void setUp() {
        promotions = new ArrayList<>();
        cart = new Cart();
        promotionProducts = new HashMap<>();
    }

    @Test(expected = RuntimeException.class)
    public void emptyProductListForMoreThanOneUnitPromotionTest() {
        promotionProducts.put(1, 3);

        PromotionAggregator aggregator = new PromotionAggregator();
        aggregator.productsInPromotion = promotionProducts;
        aggregator.priority = 1;
        aggregator.promotionAmount = 130;

        promotions.add(new MoreThanOneUnitPromotion(aggregator));
        cart.setPromotions(promotions);

        cart.getPromotionsAppliedTotalAmount();
    }

    @Test(expected = RuntimeException.class)
    public void emptyProductListForMultipleProductPromotionTest() {
        PromotionAggregator aggregator = new PromotionAggregator();
        aggregator.productsInPromotion = null;
        aggregator.priority = 1;
        aggregator.promotionAmount = 20;

        promotions.add(new MultipleProductPromotion(aggregator));
        cart.setPromotions(promotions);

        cart.getPromotionsAppliedTotalAmount();
    }

    @Test(expected = RuntimeException.class)
    public void invalidItemInCartTest() {
        promotionProducts.put(1, 3);

        PromotionAggregator aggregator = new PromotionAggregator();
        aggregator.productsInPromotion = promotionProducts;
        aggregator.priority = 1;
        aggregator.promotionAmount = 130;

        promotions.add(new MoreThanOneUnitPromotion(aggregator));
        cart.setPromotions(promotions);

        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(null);
        cart.addItem(new ProductA());

        cart.getPromotionsAppliedTotalAmount();
    }

    @Test(expected = RuntimeException.class)
    public void invalidProductCountInPromotionlist() {
        promotionProducts.put(1, 3); //3A
        promotionProducts.put(2, 1); //B, this is invalid. This promotion type can only one product in its map

        PromotionAggregator aggregator = new PromotionAggregator();
        aggregator.productsInPromotion = promotionProducts;
        aggregator.priority = 1;
        aggregator.promotionAmount = 130;

        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        //3A=B
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());

        cart.setPromotions(promotions);

        cart.getPromotionsAppliedTotalAmount();
    }

    //Promotions:
    //3A=130
    //4B=100
    @Test
    public void moreThanOneUnitPromotionTest1() {
        promotionProducts.put(1, 3);
        PromotionAggregator aggregator1 = new PromotionAggregator();
        aggregator1.productsInPromotion = promotionProducts;
        aggregator1.priority = 1;
        aggregator1.promotionAmount = 130;

        promotions.add(new MoreThanOneUnitPromotion(aggregator1));

        Map<Integer, Integer> promotionProducts2 = new HashMap<>();
        promotionProducts2.put(2, 4);
        PromotionAggregator aggregator2 = new PromotionAggregator();
        aggregator2.productsInPromotion = promotionProducts2;
        aggregator2.priority = 1;
        aggregator2.promotionAmount = 100;

        promotions.add(new MoreThanOneUnitPromotion(aggregator2));

        //Products in cart
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

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 340.0, 0.001);
    }

    //Promotions
    //C+D=30
    @Test
    public void multipleProductPromotionTest1() {
        promotionProducts.put(3, 1);
        promotionProducts.put(4, 1);
        PromotionAggregator aggregator = new PromotionAggregator();
        aggregator.productsInPromotion = promotionProducts;
        aggregator.priority = 1;
        aggregator.promotionAmount = 30;

        promotions.add(new MultipleProductPromotion(aggregator));

        //Products in cart
        //A+B+C+D
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductB());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 110.0, 0.001);
    }

    //Promotions
    //3A=130
    //2B=50
    //C+D=30
    @Test
    public void mixPromotionsTest1() {
        Map<Integer, Integer> promotionProducts2 = new HashMap<>();
        promotionProducts2.put(1, 3);
        PromotionAggregator aggregator1 = new PromotionAggregator();
        aggregator1.productsInPromotion = promotionProducts2;
        aggregator1.priority = 1;
        aggregator1.promotionAmount = 130;

        promotions.add(new MoreThanOneUnitPromotion(aggregator1));

        Map<Integer, Integer> promotionProducts3 = new HashMap<>();
        promotionProducts3.put(2, 2);
        PromotionAggregator aggregator2 = new PromotionAggregator();
        aggregator2.productsInPromotion = promotionProducts3;
        aggregator2.priority = 1;
        aggregator2.promotionAmount = 50;

        promotions.add(new MoreThanOneUnitPromotion(aggregator2));

        promotionProducts.put(3, 1);
        promotionProducts.put(4, 1);
        PromotionAggregator aggregator3 = new PromotionAggregator();
        aggregator3.productsInPromotion = promotionProducts;
        aggregator3.priority = 1;
        aggregator3.promotionAmount = 30;

        promotions.add(new MultipleProductPromotion(aggregator3));

        //Products in cart
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

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 340.0, 0.001);
    }

    //Promotions
    //3A+2B+C=180
    @Test
    public void multipleProductPromotionTest2() {
        promotionProducts.put(1, 3);
        promotionProducts.put(2, 2);
        promotionProducts.put(3, 1);
        PromotionAggregator aggregator = new PromotionAggregator();
        aggregator.productsInPromotion = promotionProducts;
        aggregator.priority = 1;
        aggregator.promotionAmount = 180;

        promotions.add(new MultipleProductPromotion(aggregator));

        //Products in cart
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

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 295.0, 0.001);
    }

    //Promotions
    //A+B+3C+4D=210
    @Test
    public void multipleProductPromotionTest3() {
        promotionProducts.put(1, 1); //A
        promotionProducts.put(2, 1); //2B
        promotionProducts.put(3, 3); //3C
        promotionProducts.put(4, 4); //4D
        PromotionAggregator aggregator = new PromotionAggregator();
        aggregator.productsInPromotion = promotionProducts;
        aggregator.priority = 1;
        aggregator.promotionAmount = 210;

        promotions.add(new MultipleProductPromotion(aggregator));

        //Products in cart
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

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 490, 0.001);
    }

    //Promotions
    //3C+4D=75
    //3A=130
    //2B=50
    @Test
    public void mixPromotionsTest2() {
        promotionProducts.put(3, 3); //3C
        promotionProducts.put(4, 4); //4D
        PromotionAggregator aggregator1 = new PromotionAggregator();
        aggregator1.productsInPromotion = promotionProducts;
        aggregator1.priority = 1;
        aggregator1.promotionAmount = 75;

        promotions.add(new MultipleProductPromotion(aggregator1));

        Map<Integer, Integer> promotionProducts2 = new HashMap<>();
        promotionProducts2.put(1, 3);
        PromotionAggregator aggregator2 = new PromotionAggregator();
        aggregator2.productsInPromotion = promotionProducts2;
        aggregator2.priority = 1;
        aggregator2.promotionAmount = 130;

        promotions.add(new MoreThanOneUnitPromotion(aggregator2));

        Map<Integer, Integer> promotionProducts3 = new HashMap<>();
        promotionProducts3.put(2, 2);
        PromotionAggregator aggregator3 = new PromotionAggregator();
        aggregator3.productsInPromotion = promotionProducts3;
        aggregator3.priority = 1;
        aggregator3.promotionAmount = 50;

        promotions.add(new MoreThanOneUnitPromotion(aggregator3));

        //Products in cart
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

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 405, 0.001);
    }

    //Promotions
    //3A+2B+C=180
    @Test
    public void multipleProductPromotionTest4() {
        promotionProducts.put(1, 3);
        promotionProducts.put(2, 2);
        promotionProducts.put(3, 1);
        PromotionAggregator aggregator = new PromotionAggregator();
        aggregator.productsInPromotion = promotionProducts;
        aggregator.priority = 1;
        aggregator.promotionAmount = 180;

        promotions.add(new MultipleProductPromotion(aggregator));

        //Products in cart
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

    //Promotions
    //3A+2B+C=180
    @Test
    public void multipleProductPromotionTest5() {
        promotionProducts.put(1, 3);
        promotionProducts.put(2, 2);
        promotionProducts.put(3, 1);
        PromotionAggregator aggregator = new PromotionAggregator();
        aggregator.productsInPromotion = promotionProducts;
        aggregator.priority = 1;
        aggregator.promotionAmount = 180;

        promotions.add(new MultipleProductPromotion(aggregator));

        //Products in cart
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

    //Promotions
    //3A+2B+C=180
    //3A=130
    @Test
    public void mixPromotionsTestWithHigherPriority() {
        promotionProducts.put(1, 3);
        promotionProducts.put(2, 2);
        promotionProducts.put(3, 1);
        PromotionAggregator aggregator1 = new PromotionAggregator();
        aggregator1.productsInPromotion = promotionProducts;
        aggregator1.priority = 1;
        aggregator1.promotionAmount = 180;

        promotions.add(new MultipleProductPromotion(aggregator1));

        Map<Integer, Integer> promotionProducts2 = new HashMap<>();
        promotionProducts2.put(1, 3);
        PromotionAggregator aggregator2 = new PromotionAggregator();
        aggregator2.productsInPromotion = promotionProducts2;
        aggregator2.priority = 2;
        aggregator2.promotionAmount = 130;

        promotions.add(new MoreThanOneUnitPromotion(aggregator2));

        //Products in cart
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

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 325.0, 0.001);
    }

    //Promotions
    //3A+2B+C=180
    //3A=130
    @Test
    public void mixPromotionsTestWithSamePriority() {
        promotionProducts.put(1, 3);
        promotionProducts.put(2, 2);
        promotionProducts.put(3, 1);
        PromotionAggregator aggregator1 = new PromotionAggregator();
        aggregator1.productsInPromotion = promotionProducts;
        aggregator1.priority = 1;
        aggregator1.promotionAmount = 180;

        promotions.add(new MultipleProductPromotion(aggregator1));

        Map<Integer, Integer> promotionProducts2 = new HashMap<>();
        promotionProducts2.put(1, 3);
        PromotionAggregator aggregator2 = new PromotionAggregator();
        aggregator2.productsInPromotion = promotionProducts2;
        aggregator2.priority = 1;
        aggregator2.promotionAmount = 130;

        promotions.add(new MoreThanOneUnitPromotion(aggregator2));

        //Products in cart
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

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 295.0, 0.001);
    }

    //Promotions
    //3A=130
    //2B=45
    //C+D=30
    @Test
    public void mixPromotionsTest3() {
        Map<Integer, Integer> promotionProducts2 = new HashMap<>();
        promotionProducts2.put(1, 3); //3A
        PromotionAggregator aggregator2 = new PromotionAggregator();
        aggregator2.productsInPromotion = promotionProducts2;
        aggregator2.priority = 1;
        aggregator2.promotionAmount = 130;

        promotions.add(new MoreThanOneUnitPromotion(aggregator2));

        Map<Integer, Integer> promotionProducts3 = new HashMap<>();
        promotionProducts3.put(2, 2);//2B
        PromotionAggregator aggregator3 = new PromotionAggregator();
        aggregator3.productsInPromotion = promotionProducts3;
        aggregator3.priority = 1;
        aggregator3.promotionAmount = 45;

        promotions.add(new MoreThanOneUnitPromotion(aggregator3));

        promotionProducts.put(3, 1);
        promotionProducts.put(4, 1);
        PromotionAggregator aggregator1 = new PromotionAggregator();
        aggregator1.productsInPromotion = promotionProducts;
        aggregator1.priority = 1;
        aggregator1.promotionAmount = 30;

        promotions.add(new MultipleProductPromotion(aggregator1));

        //Products in cart
        //A+B+C
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 100.0, 0.001);
    }

    //Promotions
    //3A=130
    //2B=45
    //C+D=30
    @Test
    public void mixPromotionsTest4() {
        Map<Integer, Integer> promotionProducts2 = new HashMap<>();
        promotionProducts2.put(1, 3); //3A
        PromotionAggregator aggregator2 = new PromotionAggregator();
        aggregator2.productsInPromotion = promotionProducts2;
        aggregator2.priority = 1;
        aggregator2.promotionAmount = 130;

        promotions.add(new MoreThanOneUnitPromotion(aggregator2));

        Map<Integer, Integer> promotionProducts3 = new HashMap<>();
        promotionProducts3.put(2, 2);//2B
        PromotionAggregator aggregator3 = new PromotionAggregator();
        aggregator3.productsInPromotion = promotionProducts3;
        aggregator3.priority = 1;
        aggregator3.promotionAmount = 45;

        promotions.add(new MoreThanOneUnitPromotion(aggregator3));

        promotionProducts.put(3, 1); //C
        promotionProducts.put(4, 1); //D
        PromotionAggregator aggregator1 = new PromotionAggregator();
        aggregator1.productsInPromotion = promotionProducts;
        aggregator1.priority = 1;
        aggregator1.promotionAmount = 30;

        promotions.add(new MultipleProductPromotion(aggregator1));

        cart.setPromotions(promotions);

        //Products in cart
        //5A+5B+C
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 370.0, 0.001);
    }

    //Promotions
    //3A=130
    //2B=45
    //C+D=30
    @Test
    public void mixPromotionsTest5() {
        Map<Integer, Integer> promotionProducts2 = new HashMap<>();
        promotionProducts2.put(1, 3); //3A
        PromotionAggregator aggregator2 = new PromotionAggregator();
        aggregator2.productsInPromotion = promotionProducts2;
        aggregator2.priority = 1;
        aggregator2.promotionAmount = 130;

        promotions.add(new MoreThanOneUnitPromotion(aggregator2));

        Map<Integer, Integer> promotionProducts3 = new HashMap<>();
        promotionProducts3.put(2, 2);//2B
        PromotionAggregator aggregator3 = new PromotionAggregator();
        aggregator3.productsInPromotion = promotionProducts3;
        aggregator3.priority = 1;
        aggregator3.promotionAmount = 45;

        promotions.add(new MoreThanOneUnitPromotion(aggregator3));

        promotionProducts.put(3, 1);
        promotionProducts.put(4, 1);
        PromotionAggregator aggregator1 = new PromotionAggregator();
        aggregator1.productsInPromotion = promotionProducts;
        aggregator1.priority = 1;
        aggregator1.promotionAmount = 30;

        promotions.add(new MultipleProductPromotion(aggregator1));

        cart.setPromotions(promotions);

        //Products in cart
        //3A+5B+C+D
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductB());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 280.0, 0.001);
    }

    @After
    public void tearDown() {
    }
}

