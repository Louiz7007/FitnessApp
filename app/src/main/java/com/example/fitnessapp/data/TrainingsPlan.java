package com.example.fitnessapp.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TrainingsPlan {

    private final ArrayList<Training> trainingsList;
    private final String name;

    public TrainingsPlan(String name) {

        this.name = name;
        trainingsList = new ArrayList<>();
    }

    public ArrayList<Training> getTrainingsList() {
        return trainingsList;
    }

    public void addTraining(Training training) {
        trainingsList.add(training);
    }

    @NonNull
    @Override
    public String toString() {
        return name + " " + trainingsList.toString();
    }

    public String getName() {
        return name;
    }
}
