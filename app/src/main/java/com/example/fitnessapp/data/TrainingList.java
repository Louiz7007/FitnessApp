package com.example.fitnessapp.data;

import java.util.ArrayList;

public class TrainingList {

    ArrayList<Training> trainingList = new ArrayList<>();

    public TrainingList() {

        this.trainingList.add(new Training(1, "Laufen", "Langsam", 9));
        this.trainingList.add(new Training(2, "Laufen", "Schnell", 14.5));
        this.trainingList.add(new Training(3, "Radfahren", "Langsam", 5.8));
        this.trainingList.add(new Training(4, "Radfahren", "Schnell", 12));
        this.trainingList.add(new Training(5, "Schwimmen", "Rücken", 9.5));
        this.trainingList.add(new Training(6, "Schwimmen", "Brust", 10.3));
        this.trainingList.add(new Training(7, "Fußball", "Normal", 7));
        this.trainingList.add(new Training(8, "Tennis", "Normal", 7.3));

    }

    public ArrayList<Training> getTrainingList() {
        return trainingList;
    }

}
