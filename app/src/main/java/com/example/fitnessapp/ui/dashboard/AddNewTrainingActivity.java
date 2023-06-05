package com.example.fitnessapp.ui.dashboard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.DBOpenHelper;
import com.example.fitnessapp.MainActivity;
import com.example.fitnessapp.data.Training;
import com.example.fitnessapp.databinding.AddNewTrainingBinding;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddNewTrainingActivity extends AppCompatActivity {

    Timestamp timestamp;
    private AddNewTrainingBinding binding;
    private String date;
    private String time;
    private DBOpenHelper helper;
    private SimpleDateFormat timeFormat;
    private Calendar calendar;

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
        calendar = Calendar.getInstance();

        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        binding.datePicker.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1,
                                                                            dayOfMonth1) -> {
                String day = "";
                String month2 = "";
                month1++;
                if (dayOfMonth1 < 10) {
                    day = 0 + String.valueOf(dayOfMonth1);
                }
                else {
                    day = String.valueOf(dayOfMonth1);
                }
                if (month1 < 10) {
                    month2 = 0 + String.valueOf(month1);
                }
                else {
                    month2 = String.valueOf(month1);
                }


                date = year1 + "-" + month2 + "-" + day;


                binding.datePicker.setText(date);
            }, year, month, dayOfMonth);

            datePickerDialog.show();
        });

        binding.timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddNewTrainingActivity.this,
                        (view, hourOfDay, minute1) -> {
                            // Wird aufgerufen, wenn der Benutzer eine Zeit auswählt
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute1);
                            String selectedTime = timeFormat.format(calendar.getTime());
                            time = selectedTime;
                            binding.timePicker.setText(selectedTime);
                        },
                        hour,
                        minute,
                        true
                );

                // Zeige den TimePickerDialog an
                timePickerDialog.show();
            }
        });


        binding.writeToDBBtn.setOnClickListener(v -> {
            if (binding.trainingDuration.getText().toString().isEmpty() || binding.datePicker.getText().toString().isEmpty() || binding.timePicker.getText().toString().isEmpty()) {
                Snackbar.make(v, "Geben Sie alle Felder an!", Snackbar.LENGTH_LONG).show();
                return;
            }
            training.setDuration(Integer.parseInt(binding.trainingDuration.getText().toString()));
            timestamp = Timestamp.valueOf(date + " " + time + ":00.000000000");
            training.setDate(timestamp);
            helper.insertUserTrainingWithTrainingObject(training);

            clearBackStack();
            startActivity(new Intent(this, MainActivity.class));
            showSuccessMsg(v);

        });


    }

    private void showSuccessMsg(View v) {
        Snackbar.make(v, "Training wurde erfolgreich hinzugefügt!",
                      Snackbar.LENGTH_LONG).setAction("X", s -> {
        }).show();
    }

    private void clearBackStack() {
        finish();
    }

}
