package com.example.fitnessapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitnessapp.DBOpenHelper;
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
    private DBOpenHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentNewTraingsPlanWithTrainingBinding.inflate(getLayoutInflater());
        helper = new DBOpenHelper(getContext());

        ArrayList<Training> trainingList = new TrainingList().getTrainingList();

        AdapterTrainingWithCheckbox adapterTraining =
                new AdapterTrainingWithCheckbox(binding.getRoot().getContext(), trainingList);

        binding.listViewWithCheckboxes.setAdapter(adapterTraining);


        String name = getArguments().getString("trainingName");

        TrainingsPlan trainingsPlan = new TrainingsPlan(name);


        binding.createTrainingsPlanBtn.setOnClickListener(v -> {

            if (adapterTraining.getSelectedTrainingList().isEmpty()) {
                Snackbar.make(v, "Fügen Sie mindestens ein Training hinzu!", Snackbar.LENGTH_LONG).show();
            }
            else {

                adapterTraining.getSelectedTrainingList().forEach(trainingsPlan::addTraining);

                helper.inserTrainingsplanWithObject(trainingsPlan);
                showSuccessMsg();
                clearBackStack();
                Navigation.findNavController(v).navigate(R.id.navigation_dashboard);
            }
        });


    }

    private void clearBackStack() {

        Navigation.findNavController(binding.getRoot()).popBackStack(R.id.navigation_dashboard,
                                                                     false);
    }

    private void showSuccessMsg() {
        Snackbar.make(binding.getRoot(), R.string.trainingsplan_success_msg,
                      Snackbar.LENGTH_LONG).show();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }
}