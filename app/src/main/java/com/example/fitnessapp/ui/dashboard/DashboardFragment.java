package com.example.fitnessapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.fitnessapp.DBOpenHelper;
import com.example.fitnessapp.R;
import com.example.fitnessapp.data.AdapterTrainingPlan;
import com.example.fitnessapp.databinding.FragmentDashboardBinding;
import com.google.android.material.snackbar.Snackbar;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DBOpenHelper helper;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater);
        helper = new DBOpenHelper(getContext());


        binding.infoTrainingsPlanBtn.setOnClickListener(this::showInfoForNewTrainingDay);


        binding.newTrainingDayBtn.setOnClickListener(v -> Navigation.findNavController(v).
                navigate(DashboardFragmentDirections.actionNavigationDashboardToNewTrainingPlanFragment()));

        binding.infoTrainingBtn.setOnClickListener(this::showInfoForNewTraining);

        binding.newTraingBtn.setOnClickListener(v -> Navigation.findNavController(v).
                navigate(DashboardFragmentDirections.actionNavigationDashboardToNewTrainingFragment()));


        AdapterTrainingPlan adapterTrainingPlan = new AdapterTrainingPlan(getContext(),
                                                                          helper.selectAllFromTrainingsplan()
        );

        Bundle bundle = new Bundle();

        binding.listViewTrainingPlan.setAdapter(adapterTrainingPlan);

        binding.listViewTrainingPlan.setOnItemClickListener((parent, view, position, id) -> {
            String name = adapterTrainingPlan.getName(position);
            bundle.putString("trainingPlan", name);

            Navigation.findNavController(view).navigate(R.id.action_navigation_dashboard_to_addTrainingFromTrainingsPlanFragment, bundle);

        });
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showInfoForNewTraining(View view) {
        Snackbar.make(view, R.string.snackbar_training_info, Snackbar.LENGTH_INDEFINITE).setAction("X", s -> {
        }).show();
    }


    private void showInfoForNewTrainingDay(View view) {

        Snackbar.make(view, R.string.snackbar_training_day_info, Snackbar.LENGTH_INDEFINITE).setAction("X", s -> {
        }).show();
    }
}