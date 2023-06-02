package com.example.fitnessapp.data;

import java.io.Serializable;
import java.sql.Timestamp;

public class Training implements Serializable {


    private final int id;
    private final String name;
    private String intensity;
    private double metValue;

    private boolean selected;

    private Timestamp timestamp;

    public Training(int id, String name, String type, double speed) {
        this.id = id;
        this.name = name;
        this.intensity = type;
        this.metValue = speed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIntensity(){
        return intensity;
    }

    public double getMetValue() {
        return metValue;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setDate(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public void setMetValue(double metValue) {
        this.metValue = metValue;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
