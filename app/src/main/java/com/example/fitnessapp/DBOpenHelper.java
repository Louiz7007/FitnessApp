package com.example.fitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.fitnessapp.data.TodayTraining;
import com.example.fitnessapp.data.Training;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FitnessDB";
    private static final int DATABASE_VERSION = 1;

    private final String SELECT_TRAINING_BY_NAME_AND_INTENSITY = "SELECT * FROM training WHERE trainingname = %s AND intensity = %s";
    private final String SELECT_USERTRAINING_BY_TRAINING_AND_DATE = "SELECT * FROM usertrainings WHERE date = %s AND idTraining = %s";
    private final String SELECT_USERTRAINING_BY_DATE = "SELECT usertrainings._id, trainingName, intensity, duration, metValue, success FROM trainings INNER JOIN usertrainings ON trainings._id = usertrainings.idTraining WHERE usertrainings.datetime BETWEEN \"%s 00:00:00\" AND \"%s 23:59:59\"";
    private final String CREATE_TABLE_USER = "CREATE TABLE user (_id INTEGER PRIMARY KEY, firstname VARCHAR(255), lastname VARCHAR(255), age INTEGER, weight DECIMAL(5,2), workoutlevel INTEGER CHECK(workoutlevel >= 0 AND workoutlevel < 4))";
    private final String CREATE_TABLE_TRAININGS = "CREATE TABLE trainings (_id INTEGER PRIMARY KEY, trainingName VARCHAR(255), intensity VARCHAR(255), metValue DECIMAL(3, 1))";
    private final String CREATE_TABLE_USER_TRAININGS = "CREATE TABLE usertrainings (_id INTEGER PRIMARY KEY, idTraining INTEGER, datetime TIMESTAMP, duration DECIMAL(3,2), success BOOLEAN, FOREIGN KEY (idTraining) REFERENCES trainings(_id))";
    private final String CREATE_TABLE_TRAININGSPLAN = "CREATE TABLE trainingsplan (_id INTEGER PRIMARY KEY, name VARCHAR(255), idTraining INTEGER, FOREIGN KEY (idTraining) REFERENCES trainings(_id))";
    private final String CREATE_TRAININGS = "INSERT INTO trainings VALUES" +
            "(1, \"Laufen\", \"Langsam\", 9)," +
            "(2, \"Laufen\", \"Schnell\", 14.5)," +
            "(3, \"Radfahren\", \"Langsam\", 5.8)," +
            "(4, \"Radfahren\", \"Schnell\", 12)," +
            "(5, \"Schwimmen\", \"Rücken\", 9.5)," +
            "(6, \"Schwimmen\", \"Brust\", 10.3)," +
            "(7, \"Fußball\", \"Normal\", 7)," +
            "(8, \"Tennis\", \"Normal\", 7.3)";
    public DBOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_TRAININGS);
        db.execSQL(CREATE_TABLE_TRAININGSPLAN);
        db.execSQL(CREATE_TABLE_USER_TRAININGS);
        db.execSQL(CREATE_TRAININGS);
        db.execSQL("INSERT INTO trainingsplan VALUES(1, \"Monday\", 1)");
    }

    // Returns all Entries of the trainings_Table
    public Cursor selectAllTrainings() {
        return getReadableDatabase().rawQuery("SELECT * FROM trainings", null);
    }

    // Create a new Trainingsplan
    public void insertTrainingsplan(String name, int idTraining) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("idTraining", idTraining);
        getWritableDatabase().insert("trainingsplan", null, values);
    }

    public void deleteTrainingsplanByName(String name) {
        getWritableDatabase().delete("trainingsplan", name + "=" + name,null);
    }

    public Cursor selectAllFromTrainingsplan() {
        return getWritableDatabase().rawQuery("SELECT * FROM trainingsplan", null);
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

    public void insertUserTraining(int idTraining, String datetime, boolean success) {
        ContentValues values = new ContentValues();
        values.put("idTraining", idTraining);
        values.put("datetime", datetime);
        values.put("success", success);
        long rowId = getWritableDatabase().insert("usertrainings", null, values);
        Log.d(DBOpenHelper.class.getSimpleName(), "Insert into UserTrainings_Table: " + rowId);
    }

    public void insertUserTrainingWithTrainingObject(Training training) {
        ContentValues values = new ContentValues();
        values.put("idTraining", training.getId());
        values.put("datetime", training.getTimestamp().toString());
        values.put("duration", training.getDuration());
        values.put("success", false);
        long rowId = getWritableDatabase().insert("usertrainings", null, values);
        Log.d(DBOpenHelper.class.getSimpleName(), "Insert into UserTrainings_Table: " + rowId);
    }

    // Returns Cursor with all from usertrainings Table
    public Cursor selectAllfromUserTrainings() {
        return getReadableDatabase().rawQuery("SELECT * FROM usertrainings", null);
    }

    public ArrayList<TodayTraining> selectTodaysTraining() {
        ArrayList<TodayTraining> todayTrainingsList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(String.format(SELECT_USERTRAINING_BY_DATE, Date.valueOf(String.valueOf(LocalDate.now())), Date.valueOf(String.valueOf(LocalDate.now()))), null);
        while(cursor.moveToNext()) {
            boolean success = cursor.getInt(5) == 0 ? false : true;
            todayTrainingsList.add(new TodayTraining(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), success));
        }
        return todayTrainingsList;
    }

    public void updateUsertrainingsStatus(int id, boolean success) {
        ContentValues values = new ContentValues();
        values.put("success", success);
        getWritableDatabase().update("usertrainings", values, "_id=?", new String[]{String.valueOf(id)});
    }

    public Integer selectWorkoutlevel() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT workoutlevel FROM user", null);
        if(cursor.moveToNext()){
            return cursor.getInt(0);
        }
        return 0;
    }


    public Cursor selectUserTrainingByDateAndTraining(int idTraining, Date date) {
        return getReadableDatabase().rawQuery(String.format(SELECT_USERTRAINING_BY_TRAINING_AND_DATE, String.valueOf(idTraining), String.valueOf(date)), null);
    }

    public void deleteUsertrainings() {
        getWritableDatabase().delete("usertrainings", null, null);
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

    public void deleteUser() {
        getWritableDatabase().delete("user", null, null);
    }

    public boolean userExists() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM user", null);
        boolean result = cursor.moveToNext();
        cursor.close();
        return result;
    }

}
