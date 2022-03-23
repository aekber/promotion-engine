# Promotion Engine

This project represents a basic promotion engine.Right now, there are two promotion types.
If needed more, just extend AbstractPromotion class and apply required methods.That's it.

Both promotions that are introduced here take a PromotionAggregator instance
which contains map of promotioned products, promotion amount and promotion priority.

In order to set promotion into map;

```
        //3A=130
        Map<Integer, Integer> promotionProducts2 = new HashMap<>();
        promotionProducts2.put(1, 3); //3A
        PromotionAggregator aggregator1 = new PromotionAggregator();
        aggregator1.productsInPromotion = promotionProducts2;
        aggregator1.priority = 1;
        aggregator1.promotionAmount = 130;

        promotions.add(new MoreThanOneUnitPromotion(aggregator1));
```

Key of the promotion map indicates product id that needs to be promotioned and value
of the promotion map indicates to product count that is required for promotion.Product ids
for products are;

```
A --> id: 1, price: 50
B --> id: 2, price: 30
C --> id: 3, price: 15
D --> id: 4, price: 10
```

More than one unit promotion requires to have only one product for each instance of this promotion.If more promotion from this type needs to be defined, then
create another instance of this promotion.For e.g.

```
        //3A=130
        Map<Integer, Integer> promotionProducts2 = new HashMap<>();
        promotionProducts2.put(1, 3); //3A
        PromotionAggregator aggregator1 = new PromotionAggregator();
        aggregator1.productsInPromotion = promotionProducts2;
        aggregator1.priority = 1;
        aggregator1.promotionAmount = 130;

        promotions.add(new MoreThanOneUnitPromotion(aggregator1));

        //2B=50
        Map<Integer, Integer> promotionProducts3 = new HashMap<>();
        promotionProducts3.put(2, 2); //2B
        PromotionAggregator aggregator2 = new PromotionAggregator();
        aggregator2.productsInPromotion = promotionProducts3;
        aggregator2.priority = 1;
        aggregator2.promotionAmount = 50;

        promotions.add(new MoreThanOneUnitPromotion(aggregator2));
```

For the other promotion there is no restriction like this.

It is also possible to prioritize the promotions by setting a priority value. It is useful
when we have more than one promotion for the same product. Higher priority value will be taken into
account first.

```
        PromotionAggregator aggregator2 = new PromotionAggregator();
        aggregator2.priority = 1;
```

Developed with Java 8 under Ubuntu 20.04 LTS.

# Build

Run these commands for building project and running the tests:
```
mvn clean compile
mvn test
```

# Class Diagrams
