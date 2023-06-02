package com.example.fitnessapp.ui.dashboard;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavAction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnessapp.R;
import com.example.fitnessapp.databinding.FragmentNewTrainingBinding;
import com.example.fitnessapp.databinding.FragmentNewTrainingPlanBinding;


public class NewTrainingPlanFragment extends Fragment {


    FragmentNewTrainingPlanBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentNewTrainingPlanBinding.inflate(getLayoutInflater());

        Bundle bundle = new Bundle();


        binding.nameForTrainingPlanBtn.setOnClickListener(v -> {
            bundle.putString("trainingName", binding.trainingsPlanNameEditText.getText().toString());
            Navigation.findNavController(v).navigate(R.id.action_newTrainingPlanFragment_to_newTraingsPlanWithTrainingFragment, bundle);
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return binding.getRoot();
    }
}