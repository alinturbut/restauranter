package com.alinturbut.restauranter.model;

/**
 * @author alinturbut.
 */
public abstract class MenuItem {
    public abstract String getId();

    public abstract String getCategoryId();

    public abstract String getName();

    public abstract int getPrice();
}
