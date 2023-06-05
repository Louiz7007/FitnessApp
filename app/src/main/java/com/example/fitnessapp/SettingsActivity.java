package com.example.fitnessapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.fitnessapp.data.User;
import com.example.fitnessapp.databinding.ActivitySettingsBinding;
import com.example.fitnessapp.databinding.AddNewTrainingBinding;
import com.google.android.material.snackbar.Snackbar;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private DBOpenHelper helper;

    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        helper = new DBOpenHelper(binding.getRoot().getContext());
        user = new User(helper);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.workout_levels,
                        android.R.layout.simple_spinner_item);

        binding.spinnerWorkoutlevelSettings.setAdapter(adapter);

        binding.editTextFirstnameSettings.setText(user.getFirstname());
        binding.editTextLastnameSettings.setText(user.getLastname());
        binding.editTextAgeSettings.setText(String.valueOf(user.getAge()));
        binding.editTextWeightSettings.setText(String.valueOf(user.getWeight()));
        binding.spinnerWorkoutlevelSettings.setSelection(user.getWorkoutlevel());
        binding.infoImgUpdate.setOnClickListener(this::showInfoLong);
        binding.buttonChangeProfile.setOnClickListener(v -> {
            if (checkInputValues(v)) {
                changeProfile();
                startActivity(new Intent(this, MainActivity.class));
            }
        });
        binding.buttonDeleteProfile.setOnClickListener(v -> {
            deleteProfile();
            startActivity(new Intent(this, ProfileActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = new User(helper);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private void showInfoLong(View view) {

        Snackbar.make(view, R.string.snackbar_MET_msg,
                Snackbar.LENGTH_INDEFINITE).setAction("X", s -> {
        }).show();
    }

    private void changeProfile() {
        String vorname=        binding.editTextFirstnameSettings.getText().toString();
        String nachname =        binding.editTextLastnameSettings.getText().toString();
       int age =         Integer.parseInt(binding.editTextAgeSettings.getText().toString());
       double weight =        Double.parseDouble(binding.editTextWeightSettings.getText().toString());
        int work =        getWorkoutLvl();
        helper.updateUser(
                binding.editTextFirstnameSettings.getText().toString(),
                binding.editTextLastnameSettings.getText().toString(),
                Integer.parseInt(binding.editTextAgeSettings.getText().toString()),
                Double.parseDouble(binding.editTextWeightSettings.getText().toString()),
                getWorkoutLvl()
        );
    }

    private int getWorkoutLvl() {

        String selectedValue = binding.spinnerWorkoutlevelSettings.getSelectedItem().toString();

        switch (selectedValue) {
            case "ungenügend":
                return 1;
            case "niedrig":
                return 2;
            case "mittel":
                return 3;
            case "hoch":
                return 0;

        }
        return 0;

    }

    private boolean checkInputValues(View view) {

        if (checkForBlankInput()) {
            Snackbar.make(view, R.string.blank_input_msg_alert,
                    Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkForBlankInput() {

        return binding.editTextFirstnameSettings.getText().toString().isEmpty() ||
                binding.editTextLastnameSettings.getText().toString().isEmpty() ||
                binding.editTextWeightSettings.getText().toString().isEmpty() ||
                binding.textViewAgeSettings.getText().toString().isEmpty();

    }

    private void deleteProfile(){
        helper.deleteDatabase();
    }
}