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
import com.example.fitnessapp.data.TrainingsPlan;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FitnessDB";
    private static final int DATABASE_VERSION = 1;

//    private final String SELECT_TRAINING_BY_NAME_AND_INTENSITY = "SELECT * FROM training WHERE " +
//            "trainingname = %s AND intensity = %s";
//    private final String SELECT_USERTRAINING_BY_TRAINING_AND_DATE = "SELECT * FROM usertrainings " +
//            "WHERE date = %s AND idTraining = %s";
//    private final String SELECT_USERTRAINING_BY_DATE = "SELECT usertrainings._id, trainingName, " +
//            "intensity, duration, metValue, success FROM trainings INNER JOIN usertrainings ON " +
//            "trainings._id = usertrainings.idTraining WHERE usertrainings.datetime BETWEEN \"%s " +
//            "00:00:00\" AND \"%s 23:59:59\"";
//    private final String CREATE_TABLE_USER = "CREATE TABLE user (_id INTEGER PRIMARY KEY, " +
//            "firstname VARCHAR(255), lastname VARCHAR(255), age INTEGER, weight DECIMAL(5,2), " +
//            "workoutlevel INTEGER CHECK(workoutlevel >= 0 AND workoutlevel < 4))";
//    private final String CREATE_TABLE_TRAININGS = "CREATE TABLE trainings (_id INTEGER PRIMARY " +
//            "KEY, trainingName VARCHAR(255), intensity VARCHAR(255), metValue DECIMAL(3, 1))";
//    private final String CREATE_TABLE_USER_TRAININGS = "CREATE TABLE usertrainings (_id INTEGER " +
//            "PRIMARY KEY, idTraining INTEGER, datetime TIMESTAMP, duration INTEGER, success " +
//            "BOOLEAN, FOREIGN KEY (idTraining) REFERENCES trainings(_id))";
//    private final String CREATE_TABLE_TRAININGSPLAN = "CREATE TABLE trainingsplan (_id INTEGER " +
//            "PRIMARY KEY, name VARCHAR(255), idTraining INTEGER, FOREIGN KEY (idTraining) " +
//            "REFERENCES trainings(_id))";

    private final String SELECT_SUM_METVALUE_THIS_WEEK = "SELECT metValue, duration FROM usertrainings INNER JOIN trainings ON usertrainings.idTraining = trainings._id WHERE strftime('%W', DATE(datetime)) == strftime('%W', 'now') AND success = '1';";
    private final String SELECT_TRAININGSPLAN_WITH_TRAININGS_BY_NAME = "SELECT * FROM trainingsplan INNER JOIN trainings ON trainingsplan.idTraining = trainings._id WHERE name = \"%s\";";
    private final String SELECT_TRAININGSPLAN_BY_NAME = "SELECT * FROM trainingsplan WHERE name = \"%s\"";
    private final String SELECT_TRAINING_BY_NAME_AND_INTENSITY = "SELECT * FROM training WHERE trainingname = %s AND intensity = %s";
    private final String SELECT_USERTRAINING_BY_TRAINING_AND_DATE = "SELECT * FROM usertrainings WHERE date = %s AND idTraining = %s";
    private final String SELECT_USERTRAINING_FROM_NOW = "SELECT usertrainings._id, trainingName, intensity, metValue, usertrainings.datetime, usertrainings.duration FROM trainings INNER JOIN usertrainings ON trainings._id = usertrainings.idTraining WHERE usertrainings.datetime >= datetime('now') ORDER BY usertrainings.datetime ASC";
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
//    private final String SELECT_TRAININGSPLAN_WITH_TRAININGS_BY_NAME = "SELECT * FROM " +
//            "trainingsplan " +
//            "INNER JOIN trainings ON trainingsplan.idTraining = trainings._id WHERE name = \"%s\";";
//    private final String SELECT_TRAININGSPLAN_BY_NAME = "SELECT * FROM trainingsplan WHERE name =" +
//            " '%s'";

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
        db.execSQL("INSERT INTO trainingsplan VALUES(1, \"Beispielstrainingsplan\", 1)");
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

    public void inserTrainingsplanWithObject(TrainingsPlan trainingsPlan) {
        for (Training training : trainingsPlan.getTrainingsList()) {
            ContentValues values = new ContentValues();
            values.put("name", trainingsPlan.getName());
            values.put("idTraining", training.getId());
            getWritableDatabase().insert("trainingsplan", null, values);
        }
    }

    // Update a Row in the user Table
    public void updateUser(String firstname, String lastname, int age, double weight, int workoutlevel) {
        ContentValues values = new ContentValues();
        values.put("firstname", firstname);
        values.put("lastname", lastname);
        values.put("age", age);
        values.put("weight", weight);
        values.put("workoutlevel", workoutlevel);

        SQLiteDatabase database = getWritableDatabase();
        long rowId = database.update("user", values, "firstname = ? AND lastname = ?", new String[]{firstname, lastname});
        Log.d(DBOpenHelper.class.getSimpleName(), "Update user_Table: " + rowId);
    }

    public void deleteTrainingsplanByName(String name) {
        getWritableDatabase().delete("trainingsplan", "name = ? ", new String[]{name});
    }

    public ArrayList<String> selectAllFromTrainingsplan() {

        ArrayList<String> trainingsplanList = new ArrayList<>();

        Cursor cursor = getWritableDatabase().rawQuery("SELECT DISTINCT name FROM trainingsplan",
                                                       null);
        if (cursor.getCount() == 0)  {
            trainingsplanList.add("Keine Trainingspläne vorhanden");
            return trainingsplanList;
        }
        while (cursor.moveToNext()) {
            trainingsplanList.add(cursor.getString(0));
        }

        return trainingsplanList;
    }

    public ArrayList<Training> selectAllFromTrainingsplanWithTrainingsByName(String name) {

        ArrayList<Training> trainingsList = new ArrayList<>();


        Cursor cursor =
                getReadableDatabase().rawQuery(String.format(SELECT_TRAININGSPLAN_WITH_TRAININGS_BY_NAME, name), null);

        while (cursor.moveToNext()) {
            trainingsList.add(new Training(cursor.getInt(3), cursor.getString(4),
                    cursor.getString(5), cursor.getDouble(6)));
        }
        return trainingsList;
    }

//    public Cursor selectAllFromTrainingsplanWithTrainingsByName(String name) {
//        return getReadableDatabase().rawQuery(String.format(SELECT_TRAININGSPLAN_WITH_TRAININGS_BY_NAME, name), null);
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(DBOpenHelper.class.getSimpleName(), "Upgrades werden nicht unterstützt.");
    }

    // Insert a Row into the user Table
    public void insertUser(String firstname, String lastname, int age, double weight,
                           int workoutlevel) {
        ContentValues values = new ContentValues();
        values.put("firstname", firstname);
        values.put("lastname", lastname);
        values.put("age", age);
        values.put("weight", weight);
        values.put("workoutlevel", workoutlevel);
        long rowId = getWritableDatabase().insert("user", null, values);
        Log.d(DBOpenHelper.class.getSimpleName(), "Insert into user_Table: " + rowId);
    }

    public void insertUserTraining(int idTraining, String datetime, double duration, boolean success) {
        ContentValues values = new ContentValues();
        values.put("idTraining", idTraining);
        values.put("datetime", datetime);
        values.put("duration", duration);
        values.put("success", success);
        long rowId = getWritableDatabase().insert("usertrainings", null, values);
        Log.d(DBOpenHelper.class.getSimpleName(), "Insert into UserTrainings_Table: " + rowId);
    }

    public double selectUsertrainingsThisWeek() {
        Cursor cursor = getReadableDatabase().rawQuery(SELECT_SUM_METVALUE_THIS_WEEK, null);
        double sum = 0;
        while(cursor.moveToNext()) {
            sum+= cursor.getDouble(0) * cursor.getDouble(1);
        }
        return sum;
    }

    public ArrayList<Training> selectAllUserTrainingsFromNow(){
        Cursor cursor = getReadableDatabase().rawQuery(SELECT_USERTRAINING_FROM_NOW, null);

        ArrayList<Training> trainingList = new ArrayList<>();

        while (cursor.moveToNext()) {
            Training training = new Training(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
            training.setDate(Timestamp.valueOf(cursor.getString(4)));
            training.setDuration((int) cursor.getDouble(5));
            trainingList.add(training);
        }
    return trainingList;
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
            boolean success = cursor.getInt(5) != 0;
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
        return getReadableDatabase().rawQuery(String.format(SELECT_USERTRAINING_BY_TRAINING_AND_DATE, idTraining, date), null);
    }

    public void deleteUsertrainings() {
        getWritableDatabase().delete("usertrainings", null, null);
    }

    public void deleteUsertraingsByTrainingId(int id) {
        getWritableDatabase().delete("usertrainings", "_id=?", new String[]{String.valueOf(id)});
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

    public void deleteDatabase() {
        getWritableDatabase().delete("user", null, null);
        getWritableDatabase().delete("usertrainings", null, null);
        getWritableDatabase().delete("trainingsplan", null, null);
    }

    public boolean userExists() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM user", null);
        boolean result = cursor.moveToNext();
        cursor.close();
        return result;
    }

    public boolean trainingsplanExists(String name) {
        Cursor cursor = getReadableDatabase().rawQuery(String.format(SELECT_TRAININGSPLAN_BY_NAME
                , name), null);
        boolean result = cursor.moveToNext();
        cursor.close();
        return result;
    }

    public void updateUsertraining(Training training) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("datetime", training.getTimestamp().toString());
        values.put("duration", training.getDuration());

        String whereClause = "_id = ?";
        String[] whereArgs = {String.valueOf(training.getId())};

        db.update("usertrainings", values, whereClause, whereArgs);


    }
}
