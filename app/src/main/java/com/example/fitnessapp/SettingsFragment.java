package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnessapp.databinding.FragmentSettingsBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private DBOpenHelper helper;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        binding.infoImgSettings.setOnClickListener(this::showInfoLong);
        binding.buttonChangeProfile.setOnClickListener(v -> {
            if (checkInputValues(v)) {
                changeProfile();
                //startActivity(new Intent(this, MainActivity.class));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    private void showInfoLong(View view) {

        Snackbar.make(view, R.string.snackbar_msg,
                Snackbar.LENGTH_INDEFINITE).setAction("X", s -> {
        }).show();
    }

    private void changeProfile() {
        helper.updateUser(
                binding.editTextFirstnameSettings.getText().toString(),
                binding.editTextLastnameSettings.getText().toString(),
                Integer.parseInt(binding.editTextAgeSettings.getText().toString()),
                Double.parseDouble(binding.editTextWeightSettings.getText().toString()),
                getWorkoutLvl()
        );
    }

    private int getWorkoutLvl() {

        String selectedValue = binding.spinnerWorkoutlevelSettings.getSelectedItem().toString();

        switch (selectedValue) {
            case "ungenügend":
                return 1;
            case "niedrig":
                return 2;
            case "mittel":
                return 3;
            case "hoch":
                return 4;

        }
        return 0;

    }

    private boolean checkInputValues(View view) {

        if (checkForBlankInput()) {
            Snackbar.make(view, R.string.blank_input_msg_alert,
                    Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkForBlankInput() {

        if (binding.editTextFirstnameSettings.getText().toString().isEmpty() ||
                binding.editTextLastnameSettings.getText().toString().isEmpty() ||
                binding.editTextWeightSettings.getText().toString().isEmpty() ||
                binding.textViewAgeSettings.getText().toString().isEmpty()) {

            return true;
        }
        return false;

    }
}