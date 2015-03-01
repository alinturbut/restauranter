package com.alinturbut.restauranter.model;

/**
 * @author alinturbut.
 */
public class Drink {
    private String id;
    private String type;
    private String categoryId;
    private String name;
    private String manufacturer;
    private int alcoholPercentage;
    private int price;

    public Drink(String id, String type, String categoryId, String name, String manufacturer, int alcoholPercentage, int price) {
        this.id = id;
        this.type = type;
        this.categoryId = categoryId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.alcoholPercentage = alcoholPercentage;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public void setAlcoholPercentage(int alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
