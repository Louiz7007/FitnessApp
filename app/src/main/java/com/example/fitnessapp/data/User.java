package com.example.fitnessapp.data;

import android.database.Cursor;

import com.example.fitnessapp.DBOpenHelper;

public class User {
    private int id;
    private String firstname;
    private String lastname;
    private int age;
    private double weight;
    private int workoutlevel;

    public User(DBOpenHelper helper) {
        Cursor cursor = helper.selectAllFromUser();
        if(cursor.moveToNext()) {
            this.id = cursor.getInt(0);
            this.firstname = cursor.getString(1);
            this.lastname = cursor.getString(2);
            this.age = cursor.getInt(3);
            this.weight = cursor.getDouble(4);
            this.workoutlevel = cursor.getInt(5);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getWorkoutlevel() {
        return workoutlevel;
    }

    public void setWorkoutlevel(int workoutlevel) {
        this.workoutlevel = workoutlevel;
    }
}
