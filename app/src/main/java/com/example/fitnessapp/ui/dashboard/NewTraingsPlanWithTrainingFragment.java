package com.example.fitnessapp.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnessapp.R;
import com.example.fitnessapp.data.AdapterTrainingWithCheckbox;
import com.example.fitnessapp.data.Training;
import com.example.fitnessapp.data.TrainingList;
import com.example.fitnessapp.data.TrainingsPlan;
import com.example.fitnessapp.databinding.FragmentNewTraingsPlanWithTrainingBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class NewTraingsPlanWithTrainingFragment extends Fragment {

    private FragmentNewTraingsPlanWithTrainingBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentNewTraingsPlanWithTrainingBinding.inflate(getLayoutInflater());

        ArrayList<Training> trainingList = new TrainingList().getTrainingList();

        AdapterTrainingWithCheckbox adapterTraining = new AdapterTrainingWithCheckbox(binding.getRoot().getContext(), trainingList);

        binding.listViewWithCheckboxes.setAdapter(adapterTraining);


        String name = getArguments().getString("trainingName");

        TrainingsPlan trainingsPlan = new TrainingsPlan(name);


        binding.createTrainingsPlanBtn.setOnClickListener(v -> {

            if (adapterTraining.getSelectedTrainingList().isEmpty()){
                Snackbar.make(v, "Geben Sie mindestens ein Training ein!", Snackbar.LENGTH_LONG).show();
                return;
            }

            adapterTraining.getSelectedTrainingList().forEach(training -> {


                trainingsPlan.addTraining(training);
            });

            //WRITE trainingsPlan to DB

            });



    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }
}