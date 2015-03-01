package com.alinturbut.restauranter.model;

import java.util.List;

/**
 * @author alinturbut.
 */
public class Order {
    private String id;
    private List<Drink> drinks;
    private List<Food> foods;
    private int price;

    public Order(String id, List<Drink> drinks, List<Food> foods, int price) {
        this.id = id;
        this.drinks = drinks;
        this.foods = foods;
        this.price = price;
    }

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
}
