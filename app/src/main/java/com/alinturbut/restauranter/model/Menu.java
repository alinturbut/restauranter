package com.alinturbut.restauranter.model;

import java.util.List;

/**
 * @author alinturbut.
 */
public class Menu {
    private String id;
    private List<Drink> drinks;
    private List<Food> foods;

    public Menu(String id, List<Drink> drinks, List<Food> foods) {
        this.setId(id);
        this.setDrinks(drinks);
        this.setFoods(foods);
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
}
