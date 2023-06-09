package com.example.fitnessapp.data;

public class TodayTraining {

    private final int id;
    private final String name;
    private final String intensity;
    private final double duration;
    private final double metValue;
    private boolean success;

    public TodayTraining(int id, String name, String type, double duration, double metValue, boolean success) {
        this.id = id;
        this.name = name;
        this.intensity = type;
        this.duration = duration;
        this.metValue = metValue;
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

    public double getDuration() {return duration;}
}
