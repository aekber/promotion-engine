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

import java.util.*;

import static org.junit.Assert.assertEquals;

public class PromotionTest {

    private List<IPromotion> promotions;
    private Cart cart;
    private PromotionAggregator aggregator;

    @Before
    public void setUp() {
        promotions = new ArrayList<>();
        cart = new Cart();
        aggregator = new PromotionAggregator();
    }

    @Test(expected = RuntimeException.class)
    public void emptyProductListForMoreThanOneUnitPromotionTest() {
        aggregator = getPromotionAggregator(1, 130, new Pair(1, 3));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        cart.setPromotions(promotions);
        cart.getPromotionsAppliedTotalAmount();
    }

    @Test(expected = RuntimeException.class)
    public void emptyProductListForMultipleProductPromotionTest() {
        aggregator = getPromotionAggregator(1, 20);
        promotions.add(new MultipleProductPromotion(aggregator));

        cart.setPromotions(promotions);
        cart.getPromotionsAppliedTotalAmount();
    }

    @Test(expected = RuntimeException.class)
    public void invalidItemInCartTest() {
        aggregator = getPromotionAggregator(1, 130, new Pair(1, 3));
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
        aggregator = getPromotionAggregator(1, 130, new Pair(1, 3), new Pair(2, 1));
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
        aggregator = getPromotionAggregator(1, 130, new Pair(1, 3)); //3A=130
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        aggregator = getPromotionAggregator(1, 100, new Pair(2, 4)); //4B=100
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

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
        aggregator = getPromotionAggregator(1, 30, new Pair(3, 1), new Pair(4, 1)); //C+D
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
        aggregator = getPromotionAggregator(1, 130, new Pair(1, 3));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        aggregator = getPromotionAggregator(1, 50, new Pair(2, 2));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        aggregator = getPromotionAggregator(1, 30, new Pair(3, 1), new Pair(4, 1));
        promotions.add(new MultipleProductPromotion(aggregator));

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
        aggregator = getPromotionAggregator(1, 180, new Pair(1, 3), new Pair(2, 2), new Pair(3, 1)); //3A+2B+C
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
        aggregator = getPromotionAggregator(1, 210, new Pair(1, 1), new Pair(2, 1), new Pair(3, 3), new Pair(4, 4));
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
    //A+2B+7C=200
    //5C=90
    //4A+D=190
    //3D=40
    @Test
    public void multipleProductPromotionWithDescendingPriorityTest() {
        aggregator = getPromotionAggregator(4, 200, new Pair(1, 1), new Pair(2, 2), new Pair(3, 7));
        promotions.add(new MultipleProductPromotion(aggregator));

        aggregator = getPromotionAggregator(3, 90, new Pair(3, 5));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        aggregator = getPromotionAggregator(2, 190, new Pair(1, 4), new Pair(4, 1));
        promotions.add(new MultipleProductPromotion(aggregator));

        aggregator = getPromotionAggregator(1, 40, new Pair(4, 3));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        //Products in cart
        //4A+3B+5C+6D
        cart.addItem(new ProductC());
        cart.addItem(new ProductD());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductB());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 445, 0.001);
    }

    //Promotions
    //A+2B+7C=200
    //5C=90
    //4A+D=190
    //3D=40
    @Test
    public void multipleProductPromotionWithAscendingPriorityTest() {
        aggregator = getPromotionAggregator(1, 200, new Pair(1, 1), new Pair(2, 2), new Pair(3, 7));
        promotions.add(new MultipleProductPromotion(aggregator));

        aggregator = getPromotionAggregator(2, 90, new Pair(3, 5));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        aggregator = getPromotionAggregator(3, 190, new Pair(1, 4), new Pair(4, 1));
        promotions.add(new MultipleProductPromotion(aggregator));

        aggregator = getPromotionAggregator(4, 40, new Pair(4, 3));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        //Products in cart
        //4A+3B+5C+6D
        cart.addItem(new ProductC());
        cart.addItem(new ProductD());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductB());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 460, 0.001);
    }

    //Promotions
    //A+2B+7C=200
    //5C=90
    //4A+D=190
    //3D=40
    @Test
    public void multipleProductPromotionWithRandomPriorityTest() {
        aggregator = getPromotionAggregator(1, 200, new Pair(1, 1), new Pair(2, 2), new Pair(3, 7));
        promotions.add(new MultipleProductPromotion(aggregator));

        aggregator = getPromotionAggregator(4, 90, new Pair(3, 5));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        aggregator = getPromotionAggregator(2, 190, new Pair(1, 4), new Pair(4, 1));
        promotions.add(new MultipleProductPromotion(aggregator));

        aggregator = getPromotionAggregator(3, 40, new Pair(4, 3));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        //Products in cart
        //4A+3B+7C+6D
        cart.addItem(new ProductC());
        cart.addItem(new ProductD());
        cart.addItem(new ProductB());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductB());
        cart.addItem(new ProductA());
        cart.addItem(new ProductC());
        cart.addItem(new ProductD());
        cart.addItem(new ProductA());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());
        cart.addItem(new ProductB());
        cart.addItem(new ProductD());
        cart.addItem(new ProductC());

        cart.setPromotions(promotions);

        assertEquals(cart.getPromotionsAppliedTotalAmount(), 500, 0.001);
    }

    //Promotions
    //3C+4D=75
    //3A=130
    //2B=50
    @Test
    public void mixPromotionsTest2() {
        aggregator = getPromotionAggregator(1, 75, new Pair(3, 3), new Pair(4, 4));
        promotions.add(new MultipleProductPromotion(aggregator));

        aggregator = getPromotionAggregator(1, 130, new Pair(1, 3));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        aggregator = getPromotionAggregator(1, 50, new Pair(2, 2));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

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
        aggregator = getPromotionAggregator(1, 180, new Pair(1, 3), new Pair(2, 2), new Pair(3, 1));
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
        aggregator = getPromotionAggregator(1, 180, new Pair(1, 3), new Pair(2, 2), new Pair(3, 1));
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
        aggregator = getPromotionAggregator(1, 180, new Pair(1, 3), new Pair(2, 2), new Pair(3, 1));
        promotions.add(new MultipleProductPromotion(aggregator));

        aggregator = getPromotionAggregator(2, 130, new Pair(1, 3));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

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
        aggregator = getPromotionAggregator(1, 180, new Pair(1, 3), new Pair(2, 2), new Pair(3, 1));
        promotions.add(new MultipleProductPromotion(aggregator));

        aggregator = getPromotionAggregator(1, 130, new Pair(1, 3));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

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
        aggregator = getPromotionAggregator(1, 130, new Pair(1, 3));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        aggregator = getPromotionAggregator(1, 45, new Pair(2, 2));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        aggregator = getPromotionAggregator(1, 30, new Pair(3, 1), new Pair(4, 1));
        promotions.add(new MultipleProductPromotion(aggregator));

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
        aggregator = getPromotionAggregator(1, 130, new Pair(1, 3));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        aggregator = getPromotionAggregator(1, 45, new Pair(2, 2));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        aggregator = getPromotionAggregator(1, 30, new Pair(3, 1), new Pair(4, 1));
        promotions.add(new MultipleProductPromotion(aggregator));

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
        aggregator = getPromotionAggregator(1, 130, new Pair(1, 3));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        aggregator = getPromotionAggregator(1, 45, new Pair(2, 2));
        promotions.add(new MoreThanOneUnitPromotion(aggregator));

        aggregator = getPromotionAggregator(1, 30, new Pair(3, 1), new Pair(4, 1));
        promotions.add(new MultipleProductPromotion(aggregator));

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

    private PromotionAggregator getPromotionAggregator(int priority, double promotionAmount, Pair... pairs) {
        Map<Integer, Integer> promotionProducts = new HashMap<>();
        Arrays.stream(pairs).forEach(pair -> promotionProducts.put(pair.key, pair.value));

        PromotionAggregator aggregator = new PromotionAggregator();
        aggregator.productsInPromotion = pairs.length == 0 ? null : promotionProducts;
        aggregator.priority = priority;
        aggregator.promotionAmount = promotionAmount;
        return aggregator;
    }

    private static class Pair{
        private int key;
        private int value;

        public Pair(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}

