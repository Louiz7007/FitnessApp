package com.example.fitnessapp.data;

import com.example.fitnessapp.DBOpenHelper;

import java.util.ArrayList;

public class TodayTrainingList {

    ArrayList<TodayTraining> todayTrainingsList = new ArrayList<>();

    public TodayTrainingList(DBOpenHelper helper) {
        helper.selectTodaysTraining().stream().forEach(entry -> {
            todayTrainingsList.add(entry);
        });


    }

    public ArrayList<TodayTraining> getTrainingList() {
        return todayTrainingsList;
    }

    public double getSumOfMetValue() {
        double sum = 0;
        for(TodayTraining training : this.todayTrainingsList){
            if(training.getSuccess()){
                sum+= training.getMetValue() * training.getDuration();
            }
        }
        return sum;
    }
}
