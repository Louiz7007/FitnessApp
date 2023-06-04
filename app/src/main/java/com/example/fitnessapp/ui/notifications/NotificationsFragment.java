package com.example.fitnessapp.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessapp.DBOpenHelper;
import com.example.fitnessapp.data.AdapterTrainingMonth;
import com.example.fitnessapp.data.Training;
import com.example.fitnessapp.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private DBOpenHelper helper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
    helper = new DBOpenHelper(binding.getRoot().getContext());

        AdapterTrainingMonth adapter = new AdapterTrainingMonth(getContext(), helper.selectAllUserTrainingsFromNow());

        binding.monthsListView.setAdapter(adapter);

        binding.monthsListView.setOnItemClickListener((parent, view, position, id) -> {
            Training training = (Training) adapter.getItem(position);
            startActivity(new Intent(binding.getRoot().getContext(), TrainingOptionsActivity.class)
                    .putExtra("training", training));
        });


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        AdapterTrainingMonth adapter = new AdapterTrainingMonth(getContext(), helper.selectAllUserTrainingsFromNow());

        binding.monthsListView.setAdapter(adapter);
    }
}