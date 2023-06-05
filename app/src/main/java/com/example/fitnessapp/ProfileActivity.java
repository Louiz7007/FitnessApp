package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.databinding.ActivityProfileBinding;
import com.google.android.material.snackbar.Snackbar;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private DBOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        helper = new DBOpenHelper(this);
        setContentView(view);
        checkForExisitngProfile();
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                                                R.array.workout_levels,
                                                android.R.layout.simple_spinner_item);

        binding.spinnerWorkoutlevel.setAdapter(adapter);
        binding.infoImg.setOnClickListener(this::showInfoLong);
        binding.buttonCreateProfile.setOnClickListener(v -> {
            if (checkInputValues(v)) {
                createProfile();
                this.setFinishOnTouchOutside(false);
                startActivity(new Intent(this, MainActivity.class));
            }
        });
    }

    private void checkForExisitngProfile() {
        if (helper.userExists()) {
            helper.close();
            this.setFinishOnTouchOutside(false);
            startActivity(new Intent(this, MainActivity.class));
        }

    }

    private void createProfile() {
        helper.insertUser(
                binding.editTextFirstname.getText().toString(),
                binding.editTextLastname.getText().toString(),
                Integer.parseInt(binding.editTextAge.getText().toString()),
                Double.parseDouble(binding.editTextWeight.getText().toString()),
                getWorkoutLvl()
        );
    }

    private void showInfoLong(View view) {

        Snackbar.make(view, R.string.snackbar_MET_msg,
                      Snackbar.LENGTH_INDEFINITE).setAction("X", s -> {
        }).show();
    }

    private boolean checkInputValues(View view) {

        if (checkForBlankInput()) {
            Snackbar.make(view, R.string.blank_input_msg_alert,
                          Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private int getWorkoutLvl() {

        String selectedValue = binding.spinnerWorkoutlevel.getSelectedItem().toString();

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

    private boolean checkForBlankInput() {

        return binding.editTextFirstname.getText().toString().isEmpty() ||
                binding.editTextLastname.getText().toString().isEmpty() ||
                binding.editTextWeight.getText().toString().isEmpty() ||
                binding.textViewAge.getText().toString().isEmpty();

    }
}