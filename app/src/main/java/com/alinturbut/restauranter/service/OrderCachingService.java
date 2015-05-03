package com.alinturbut.restauranter.service;

import com.alinturbut.restauranter.model.Drink;
import com.alinturbut.restauranter.model.Food;
import com.alinturbut.restauranter.model.Order;

/**
 * @author alinturbut.
 */
public class OrderCachingService {
    private Order activeOrder;
    private static OrderCachingService instance;

    public OrderCachingService(String waiterId) {
        activeOrder = new Order();
        activeOrder.setWaiterId(waiterId);
    }

    public static OrderCachingService getInstance(String waiterId) {
        if(instance == null) {
            instance = new OrderCachingService(waiterId);
        }

        return instance;
    }

    public void addDrinkToActiveOrder(Drink drink) {
        activeOrder.addDrink(drink);
    }

    public void addFoodToActiveOrder(Food food) {
        activeOrder.addFood(food);
    }

    public void removeDrinkFromActiveOrder(Drink drink) {
        activeOrder.removeDrink(drink);
    }

    public void removeFoodFromActiveOrder(Food food) {
        activeOrder.removeFood(food);
    }

    public Order getActiveOrder() {
        return this.activeOrder;
    }
}
