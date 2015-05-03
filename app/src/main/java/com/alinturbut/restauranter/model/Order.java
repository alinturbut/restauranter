package com.alinturbut.restauranter.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author alinturbut.
 */
public class Order implements Serializable {
    private String id;
    private List<Drink> drinks = new ArrayList<>();
    private List<Food> foods = new ArrayList<>();
    private int price;
    private String waiterId;

    public Order(String id, List<Drink> drinks, List<Food> foods, String waiterId) {
        this.id = id;
        this.drinks = drinks;
        this.foods = foods;
        this.price = calculatePrice();
        this.waiterId = waiterId;
    }

    public Order() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private int calculatePrice() {
        int price = 0;
        for (Food food : this.foods) {
            price += food.getPrice();
        }

        for (Drink drink : this.drinks) {
            price += drink.getPrice();
        }

        return price;
    }

    public String getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(String waiterId) {
        this.waiterId = waiterId;
    }

    public void addDrink(Drink drink) {
        this.drinks.add(drink);
    }

    public void removeDrink(Drink drink) {
        this.drinks.remove(drink);
    }

    public void addFood(Food food) {
        this.foods.add(food);
    }

    public void removeFood(Food food) {
        this.foods.remove(food);
    }
}
