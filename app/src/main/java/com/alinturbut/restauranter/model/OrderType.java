package com.alinturbut.restauranter.model;

/**
 * @author alinturbut.
 */
public enum OrderType {
    CURRENT("Current"), ACTIVE("Active"), FINISHED("Finished");

    String key;

    private OrderType(String key){
        this.key = key;
    }

    public String getKey(){
        return key;
    }
}
