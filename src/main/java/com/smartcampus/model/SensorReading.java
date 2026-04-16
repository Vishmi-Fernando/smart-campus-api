package com.smartcampus.model;

/**
 *
 * @author Vishmi
 */

public class SensorReading {

    private int id;
    private double value;

    public SensorReading() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}