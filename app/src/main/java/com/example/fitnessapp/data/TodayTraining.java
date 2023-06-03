package com.example.fitnessapp.data;

public class TodayTraining {

    private String name;
    private String intensity;
    private double metValue;
    private boolean success;

    public TodayTraining(String name, String type, double speed, boolean success) {
        this.name = name;
        this.intensity = type;
        this.metValue = speed;
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
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
