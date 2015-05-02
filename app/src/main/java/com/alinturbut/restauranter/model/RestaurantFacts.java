package com.alinturbut.restauranter.model;

import java.io.Serializable;

/**
 * @author alinturbut.
 */
public class RestaurantFacts implements Serializable {
    private long id;
    private String name;
    private String city;
    private String street;

    public RestaurantFacts(long id, String name, String city, String street) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.street = street;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
