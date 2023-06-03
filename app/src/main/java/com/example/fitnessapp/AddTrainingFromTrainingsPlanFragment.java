package com.example.fitnessapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fitnessapp.data.AdapterTrainingFromPlan;
import com.example.fitnessapp.databinding.FragmentAddTrainingFromTrainingsPlanBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddTrainingFromTrainingsPlanFragment extends Fragment {


    FragmentAddTrainingFromTrainingsPlanBinding binding;
    DBOpenHelper helper;
    private String date;
    private String time;
    private SimpleDateFormat timeFormat;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAddTrainingFromTrainingsPlanBinding.inflate(getLayoutInflater());
        helper = new DBOpenHelper(getContext());
        calendar = Calendar.getInstance();
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());


        binding.datePicker2.setOnClickListener(v -> {

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1,
                                                                                    month1,
                                                                                    dayOfMonth1) -> {
                String day = "";
                String month2 = "";
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


                binding.datePicker2.setText(date);
            }, year, month, dayOfMonth);

            datePickerDialog.show();

        });
        binding.trainingPlanText.setText(getArguments().getString("trainingPlan"));

        binding.timePicker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(),
                        (TimePickerDialog.OnTimeSetListener) (view, hourOfDay, minute1) -> {
                            // Wird aufgerufen, wenn der Benutzer eine Zeit auswählt
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute1);
                            String selectedTime = timeFormat.format(calendar.getTime());
                            time = selectedTime;
                            binding.timePicker2.setText(selectedTime);
                        },
                        hour,
                        minute,
                        true
                );

                // Zeige den TimePickerDialog an
                timePickerDialog.show();
            }
        });

        AdapterTrainingFromPlan adapterTrainingFromPlan = new AdapterTrainingFromPlan(getContext(),
                                                                                      helper.selectAllFromTrainingsplanWithTrainingsByName(
                                                                                              getArguments().getString(
                                                                                                      "trainingPlan")));

        binding.listViewTrainingInTrainingPlan.setAdapter(adapterTrainingFromPlan);

        return binding.getRoot();
    }
}