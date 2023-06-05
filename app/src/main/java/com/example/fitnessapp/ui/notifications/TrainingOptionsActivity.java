package com.example.fitnessapp.ui.notifications;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;




import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.fitnessapp.DBOpenHelper;
import com.example.fitnessapp.R;
import com.example.fitnessapp.data.Training;
import com.example.fitnessapp.databinding.TrainingOptionActivityBinding;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class TrainingOptionsActivity extends AppCompatActivity {

    TrainingOptionActivityBinding binding;
    private Calendar calendar;
    private SimpleDateFormat timeFormat;
    private SimpleDateFormat dateFormat;
    private DBOpenHelper helper;
    private Timestamp timestamp;
    private String date;
    private String time;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TrainingOptionActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        calendar = Calendar.getInstance();
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        helper = new DBOpenHelper(this);


        Intent intent = getIntent();
        Training training = (Training) intent.getSerializableExtra("training");

        binding.nameTO.setText(training.getName());
        binding.intesityTO.setText(training.getIntensity());
        binding.durationTO.setText(String.valueOf(training.getDuration()));
        binding.dateTO.setText(dateFormat.format(training.getTimestamp()));
        binding.timeTO.setText(timeFormat.format(training.getTimestamp()));


        binding.dateTO.setOnClickListener(v -> {

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(binding.getRoot().getContext(), (view, year1,
                                                                                    month1,
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


                binding.dateTO.setText(date);
            }, year, month, dayOfMonth);

            datePickerDialog.show();

        });


        binding.timeTO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        binding.getRoot().getContext(),
                        (view, hourOfDay, minute1) -> {
                            // Wird aufgerufen, wenn der Benutzer eine Zeit auswählt
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute1);
                            String selectedTime = timeFormat.format(calendar.getTime());
                            time = selectedTime;
                            binding.timeTO.setText(selectedTime);
                        },
                        hour,
                        minute,
                        true
                );

                // Zeige den TimePickerDialog an
                timePickerDialog.show();
            }
        });



        binding.cancelBtn.setOnClickListener(v -> {
            finish();
        });

        binding.updateDBBtn.setOnClickListener(v -> {
            date = binding.dateTO.getText().toString();
            time = binding.timeTO.getText().toString();
            timestamp = Timestamp.valueOf(date + " " + time + ":00.000000000");
            training.setDate(timestamp);
            training.setDuration(Integer.parseInt(binding.durationTO.getText().toString()));
            helper.updateUsertraining(training);
            finish();
        });

        binding.deleteFromDBBtn.setOnClickListener(v -> {
            helper.deleteUsertraingsByTrainingId(training.getId());
            finish();
        });
    }


}
