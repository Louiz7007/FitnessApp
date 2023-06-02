package com.example.fitnessapp.ui.dashboard;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.fitnessapp.DBOpenHelper;
import com.example.fitnessapp.R;
import com.example.fitnessapp.databinding.FragmentDashboardBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private Cursor cursor;
    private DBOpenHelper helper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DBOpenHelper(getActivity());
        cursor = helper.selectAllFromTrainingsplan();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.infoTrainingsPlanBtn.setOnClickListener(this::showInfoForNewTrainingDay);


        binding.newTrainingDayBtn.setOnClickListener(v -> Navigation.findNavController(v).
                navigate(DashboardFragmentDirections.actionNavigationDashboardToNewTrainingPlanFragment()));

        ArrayList<String> trainingList = new ArrayList<>();
        while (cursor.moveToNext()) {
            trainingList.add(cursor.getString(0) + " | " + cursor.getString(1) +
                    " | " + cursor.getString(2));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item,
                trainingList);
        binding.listViewTrainingsplan.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void showInfoForNewTrainingDay(View view) {

        Snackbar.make(view, R.string.snackbar_training_day_info, Snackbar.LENGTH_INDEFINITE).setAction("X", s -> {
        }).show();
    }
}