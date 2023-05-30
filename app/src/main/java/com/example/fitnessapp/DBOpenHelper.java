package com.example.fitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.sql.Date;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FitnessDB";
    private static final int DATABASE_VERSION = 1;

    private final String SELECT_BY_NAME = "SELECT * FROM user WHERE firstname = %s AND lastname = %s";
    private final String SELECT_TRAINING_BY_NAME_AND_INTENSITY = "SELECT * FROM training WHERE trainingname = %s AND intensity = %s";
    private final String SELECT_USERTRAINING_BY_TRAINING_AND_DATE = "SELECT * FROM usertrainings WHERE date = %s AND idTraining = %s";
    private final String SELECT_USERTRAINING_BY_DATE = "SELECT * FROM trainings INNER JOIN usertrainings ON trainings._id = usertrainings.idTraining WHERE usertrainings.date = %s";
    private final String CREATE_TABLE_USER = "CREATE TABLE user (_id INTEGER PRIMARY KEY, firstname VARCHAR(255), lastname VARCHAR(255), age INTEGER, weight DECIMAL(5,2), workoutlevel INTEGER CHECK(workoutlevel >= 0 AND workoutlevel < 4))";
    private final String CREATE_TABLE_TRAININGS = "CREATE TABLE trainings (_id INTEGER PRIMARY KEY, trainingName VARCHAR(255), intensity VARCHAR(255), metValue DECIMAL(3, 1))";
    private final String CREATE_TABLE_USER_TRAININGS = "CREATE TABLE usertrainings (_id INTEGER PRIMARY KEY, idTraining INTEGER, date DATE, success BOOLEAN, FOREIGN KEY (idTraining) REFERENCES trainings(_id))";
    public DBOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_TRAININGS);
        db.execSQL(CREATE_TABLE_USER_TRAININGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(DBOpenHelper.class.getSimpleName(), "Upgrades werden nicht unterstützt.");
    }

    // Insert a Row into the user Table
    public void insertUser(String firstname, String lastname, int age, double weight, int workoutlevel){
        ContentValues values = new ContentValues();
        values.put("firstname", firstname);
        values.put("lastname", lastname);
        values.put("age", age);
        values.put("weight", weight);
        values.put("workoutlevel", workoutlevel);
        long rowId = getWritableDatabase().insert("user", null, values);
        Log.d(DBOpenHelper.class.getSimpleName(), "Insert into user_Table: " + rowId);
    }

    // Insert a Row into the Trainings Table
    public void insertTraining(String trainingName, String intensity, double metValue) {
        ContentValues values = new ContentValues();
        values.put("trainingName", trainingName);
        values.put("intensity", intensity);
        values.put("metValue", metValue);
        long rowId = getWritableDatabase().insert("trainings", null, values);
        Log.d(DBOpenHelper.class.getSimpleName(), "Insert into Trainings_Table: " + rowId);
    }

    public void insertUserTraining(int idTraining, Date date, boolean success) {
        ContentValues values = new ContentValues();
        values.put("idTraining", idTraining);
        values.put("date", date.getTime());
        values.put("success", success);
        long rowId = getWritableDatabase().insert("usertrainings", null, values);
        Log.d(DBOpenHelper.class.getSimpleName(), "Insert into UserTrainings_Table: " + rowId);
    }

    // Returns Cursor with all from usertrainings Table
    public Cursor selectAllfromUserTrainings() {
        return getReadableDatabase().rawQuery("SELECT * FROM usertrainings", null);
    }

    public Cursor selectUserTrainingByDateAndTraining(int idTraining, Date date) {
        return getReadableDatabase().rawQuery(String.format(SELECT_USERTRAINING_BY_TRAINING_AND_DATE, String.valueOf(idTraining), String.valueOf(date)), null);
    }

    // Returns Cursor with all from usertrainings inner joined on trainings by Date
    public Cursor selectUserTrainingByDate(Date date) {
        return getReadableDatabase().rawQuery(String.format(SELECT_USERTRAINING_BY_DATE, String.valueOf(date)), null);
    }

    // Returns Cursor with all from trainings Table
    public Cursor selectAllFromTrainings() {
        return getReadableDatabase().rawQuery("SELECT * FROM trainings", null);
    }

    // Select Training by Name and Intensity
    public Cursor selectTrainingByNameAndIntensity(String trainingName, String intensity) {
        return getReadableDatabase().rawQuery(String.format(SELECT_TRAINING_BY_NAME_AND_INTENSITY, trainingName, intensity), null);
    }

    // Returns Cursor with all from user Table
    public Cursor selectAllFromUser(){
        return getReadableDatabase().rawQuery("SELECT * FROM user", null);
    }

    // Returns Cursor of User Row called by first and lastname
    public Cursor selectUserByName(String firstname, String lastname) {
        return getReadableDatabase().rawQuery(String.format(SELECT_BY_NAME, firstname, lastname), null);
    }

    public boolean userExists() {
        boolean result;
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM user", null);
        result = cursor.moveToNext();
        cursor.close();
        return result;
    }

    // Drop whole user Table
    public void dropTableUser() {
        getWritableDatabase().rawQuery("DROP TABLE IF EXISTS user", null);
    }
}
