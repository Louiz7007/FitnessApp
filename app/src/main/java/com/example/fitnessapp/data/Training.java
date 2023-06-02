package com.example.fitnessapp.data;

public class Training {


    private int id;
    private String name;
    private String intensity;
    private double metValue;

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

}
