package com.example.fitnessapp.data;

public class TodayTraining {

    private int id;
    private String name;
    private String intensity;
    private double metValue;
    private boolean success;

    public TodayTraining(int id, String name, String type, double speed, boolean success) {
        this.id = id;
        this.name = name;
        this.intensity = type;
        this.metValue = speed;
        this.success = success;
    }

    public int getId() {return id;}

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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
