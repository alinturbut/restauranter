package com.alinturbut.restauranter.model;

import java.io.Serializable;

/**
 * @author alinturbut.
 */
public class Waiter implements Serializable {
    private String _id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private int monthsOfExperience;
    private String role;

    public Waiter(String _id, String firstName, String lastName, String username, String password, int monthsOfExperience, String role) {
        this.setId(_id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.monthsOfExperience = monthsOfExperience;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMonthsOfExperience() {
        return monthsOfExperience;
    }

    public void setMonthsOfExperience(int monthsOfExperience) {
        this.monthsOfExperience = monthsOfExperience;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }
}
