package com.example.fitnessapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessapp.R;
import com.example.fitnessapp.databinding.FragmentDashboardBinding;
import com.google.android.material.snackbar.Snackbar;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.infoTrainingBtn.setOnClickListener(this::showInfoForNewTraining);
        binding.infoTrainingsPlanBtn.setOnClickListener(this::showInfoForNewTrainingDay);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showInfoForNewTraining(View view) {

        Snackbar.make(view, R.string.snackbar_training_info,
                      Snackbar.LENGTH_INDEFINITE).setAction("X", s -> {
        }).show();
    }

    private void showInfoForNewTrainingDay(View view) {

        Snackbar.make(view, R.string.snackbar_training_day_info,
                      Snackbar.LENGTH_INDEFINITE).setAction("X", s -> {
        }).show();
    }
}