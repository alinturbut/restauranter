package com.alinturbut.restauranter.model;

import java.io.Serializable;

/**
 * @author alinturbut.
 */
public class Table implements Serializable {
    private String _id;
    private int tableNumber;
    private int seats;
    private boolean isOccupied;

    public Table(){}

    public Table(String _id, int tableNumber, int seats, boolean isOccupied){ this.set_id(_id); this.setTableNumber(tableNumber);
        this.setSeats(seats); this.setIsOccupied(isOccupied);}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }
}
