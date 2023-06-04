package com.example.fitnessapp.ui.dashboard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitnessapp.DBOpenHelper;
import com.example.fitnessapp.R;
import com.example.fitnessapp.data.AdapterTrainingFromPlan;
import com.example.fitnessapp.data.Training;
import com.example.fitnessapp.databinding.FragmentAddTrainingFromTrainingsPlanBinding;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Timestamp;
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
    Timestamp timestamp;

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
                        (view, hourOfDay, minute1) -> {
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

        binding.writeTrningsToDbBtn.setOnClickListener(v -> {
            if (binding.datePicker2.getText().toString().isEmpty()||binding.timePicker2.getText().toString().isEmpty()||checkAllDurationInputFields()){
                Snackbar.make(v, "Alle Felder müssen befüllt werden!", Snackbar.LENGTH_LONG).show();
                return;
            }
            timestamp = Timestamp.valueOf(date + " " + time + ":00.000000000");

            for (int i = 0; i < binding.listViewTrainingInTrainingPlan.getChildCount(); i++) {
                Training training = (Training) binding.listViewTrainingInTrainingPlan.getItemAtPosition(i);
                training.setDate(timestamp);
                EditText durationEditText = binding.listViewTrainingInTrainingPlan.getChildAt(i).findViewById(R.id.durationLV2);
                training.setDuration(Integer.parseInt((durationEditText.getText().toString())));
                helper.insertUserTrainingWithTrainingObject(training);

            }
            Snackbar.make(v, "Trainings wurden erfolgreich hinzugefügt!", Snackbar.LENGTH_LONG).show();
            clearBackStack();
            Navigation.findNavController(v).navigate(R.id.navigation_dashboard);
        });

        return binding.getRoot();
    }

    private boolean checkAllDurationInputFields() {

for ( int i = 0; i < binding.listViewTrainingInTrainingPlan.getChildCount(); i++) {

    View childView = binding.listViewTrainingInTrainingPlan.getChildAt(i);
    EditText durationEditText = childView.findViewById(R.id.durationLV2);
    if(durationEditText.getText().toString().isEmpty() || Integer.parseInt(durationEditText.getText().toString()) == 0){
        return true;


    }

}
    return false;
    }

    private void clearBackStack() {

        Navigation.findNavController(binding.getRoot()).popBackStack(R.id.navigation_dashboard,
                false);
    }


}