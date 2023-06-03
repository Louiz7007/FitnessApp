package com.example.fitnessapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.fitnessapp.DBOpenHelper;
import com.example.fitnessapp.data.AdapterTodayTraining;
import com.example.fitnessapp.data.AdapterTraining;
import com.example.fitnessapp.data.TodayTraining;
import com.example.fitnessapp.data.TodayTrainingList;
import com.example.fitnessapp.data.Training;
import com.example.fitnessapp.data.TrainingList;
import com.example.fitnessapp.databinding.FragmentHomeBinding;
import com.example.fitnessapp.databinding.FragmentNewTrainingBinding;
import com.example.fitnessapp.databinding.FragmentTodayTrainingBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class TodayTrainingFragment extends Fragment {

    FragmentTodayTrainingBinding binding;
    private DBOpenHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FragmentTodayTrainingBinding.inflate(getLayoutInflater());

        helper = new DBOpenHelper(getContext());
        TodayTrainingList trainingList = new TodayTrainingList(helper);

        AdapterTodayTraining adapterTraining = new AdapterTodayTraining(binding.getRoot().getContext(), trainingList.getTrainingList(), helper);

        binding.listviewTrainings.setAdapter(adapterTraining);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return binding.getRoot();
    }
}