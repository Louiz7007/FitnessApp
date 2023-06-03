package com.example.fitnessapp.ui.dashboard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.DBOpenHelper;
import com.example.fitnessapp.data.Training;
import com.example.fitnessapp.databinding.AddNewTrainingBinding;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Timestamp;
import java.util.Calendar;

public class AddNewTrainingActivity extends AppCompatActivity{

    private AddNewTrainingBinding binding;
    private String date;
    private DBOpenHelper helper;
    Timestamp timestamp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddNewTrainingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        helper = new DBOpenHelper(this);

        Intent intent = getIntent();
        Training training = (Training) intent.getSerializableExtra("training");

        binding.trainingName.setText(training.getName());
        binding.trainingIntensity.setText(training.getIntensity());
        binding.trainingMetValue.setText(String.valueOf(training.getMetValue()));

        binding.editTextDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth1) -> {
                String day = "";
                String month2 = "";
                if (dayOfMonth1 < 10) {
                    day = 0 + String.valueOf(dayOfMonth1);
                }
                if (month1 < 10) {
                    month2 = 0 + String.valueOf(month1);
                }


                date = year1 + "-" + month2 + "-" + day;
                timestamp = Timestamp.valueOf(date + " 00:00:00.000000000");
                training.setDate(timestamp);

                binding.editTextDate.setText(date);
            }, year, month, dayOfMonth);

            datePickerDialog.show();
        });

        binding.writeToDBBtn.setOnClickListener(v -> {
            if (binding.trainingDuration.getText().toString().isEmpty()) {
                Snackbar.make(v, "Geben Sie eine Dauer ein!", Snackbar.LENGTH_LONG).show();
                return;
            }
            training.setMetValue(Double.parseDouble(binding.trainingDuration.getText().toString()) * training.getMetValue());
            //helper.insertUserTraining(training);
        });



    }

}
