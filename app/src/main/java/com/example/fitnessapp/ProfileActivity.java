package com.example.fitnessapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.databinding.ActivityProfileBinding;
import com.google.android.material.snackbar.Snackbar;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                                                R.array.workout_levels,
                                                android.R.layout.simple_spinner_item);

        binding.spinnerWorkoutlevel.setAdapter(adapter);
        binding.infoImg.setOnClickListener(v -> showInfo(v));

    }

    private void showInfo(View view) {

        Snackbar.make(view, R.string.snackbar_msg,
                      Snackbar.LENGTH_INDEFINITE).setAction("X", s -> {
        }).show();
    }


}