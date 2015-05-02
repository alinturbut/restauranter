package com.alinturbut.restauranter.model;

import java.io.Serializable;

/**
 * @author alinturbut.
 */
public class Category implements Serializable {
    private String _id;
    private String name;

    public Category(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
