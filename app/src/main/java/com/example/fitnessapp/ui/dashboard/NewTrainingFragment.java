package com.example.fitnessapp.ui.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.data.AdapterTraining;
import com.example.fitnessapp.data.Training;
import com.example.fitnessapp.data.TrainingList;
import com.example.fitnessapp.databinding.FragmentNewTrainingBinding;

import java.util.ArrayList;


public class NewTrainingFragment extends Fragment {

    FragmentNewTrainingBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentNewTrainingBinding.inflate(getLayoutInflater());


        ArrayList<Training> trainingList = new TrainingList().getTrainingList();


        AdapterTraining adapterTraining = new AdapterTraining( binding.getRoot().getContext(), trainingList);

        binding.listviewTrainings.setAdapter(adapterTraining);
        binding.listviewTrainings.setOnItemClickListener((parent, view, position, id) -> {
            Training training = trainingList.get(position);
            binding.textView.setText(training.getName());
        });
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }
}