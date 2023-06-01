package com.example.fitnessapp.ui.home;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessapp.DBOpenHelper;
import com.example.fitnessapp.R;
import com.example.fitnessapp.databinding.FragmentHomeBinding;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ListView listView;
    private DBOpenHelper helper;
    private Cursor cursor;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DBOpenHelper(getActivity());

        for(int i = 0; i < 6; i++) {
            helper.testDatensatz();
            helper.testDatensatzZwei();
        }
        cursor = helper.selectTodaysTraining();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.viewProgress;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        cursor = helper.selectTodaysTraining();
        ArrayList<String> trainingList = new ArrayList<>();
        while(cursor.moveToNext()) {
            String success = + cursor.getInt(3) == 0 ? "Offen" : "Beendet";
            trainingList.add(cursor.getString(0)+ " | " + cursor.getString(1) +
                    " | " + cursor.getString(2) + " | " + success);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, trainingList);
        binding.trainingsListView.setAdapter(adapter);
        binding.viewProgress.setOnClickListener(v -> {
            helper.deleteUserTrainingAndTrainings();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}