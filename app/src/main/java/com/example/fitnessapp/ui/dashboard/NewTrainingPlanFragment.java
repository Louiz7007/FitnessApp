package com.example.fitnessapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitnessapp.DBOpenHelper;
import com.example.fitnessapp.R;
import com.example.fitnessapp.databinding.FragmentNewTrainingPlanBinding;
import com.google.android.material.snackbar.Snackbar;


public class NewTrainingPlanFragment extends Fragment {


    FragmentNewTrainingPlanBinding binding;
    DBOpenHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentNewTrainingPlanBinding.inflate(getLayoutInflater());
        helper = new DBOpenHelper(getContext());
        Bundle bundle = new Bundle();


        binding.nameForTrainingPlanBtn.setOnClickListener(v -> {
            if (helper.trainingsplanExists(binding.trainingsPlanNameEditText.getText().toString())) {
                showWarningMsg();
                return;
            }

            bundle.putString("trainingName",
                             binding.trainingsPlanNameEditText.getText().toString());
            Navigation.findNavController(v).navigate(R.id.action_newTrainingPlanFragment_to_newTraingsPlanWithTrainingFragment, bundle);
        });

    }

    private void showWarningMsg() {

        Snackbar.make(binding.getRoot(), getString(R.string.trainingsplan_exists_msg) +
                " einen anderen Namen ein!", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return binding.getRoot();
    }
}