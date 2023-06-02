package com.example.fitnessapp.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessapp.DBOpenHelper;
import com.example.fitnessapp.R;
import com.example.fitnessapp.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final int REQUEST_LOCATION = 12345;
    LocationManager manager;
    LocationListener listener;
    private FragmentHomeBinding binding;
    private DBOpenHelper helper;
    private Cursor cursor;
    private Thread thread;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DBOpenHelper(getActivity());

        helper.testDatensatzZwei();
        cursor = helper.selectTodaysTraining();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        manager = getActivity().getSystemService(LocationManager.class);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final TextView textView = binding.viewProgress;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        cursor = helper.selectTodaysTraining();
        ArrayList<String> trainingList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String success = +cursor.getInt(3) == 0 ? "Offen" : "Beendet";
            trainingList.add(cursor.getString(0) + " | " + cursor.getString(1) +
                                     " | " + cursor.getString(2) + " | " + success);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item,
                                                          trainingList);
        binding.trainingsListView.setAdapter(adapter);
        binding.viewProgress.setOnClickListener(v -> {
            helper.deleteUserTrainingAndTrainings();
        });

        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!thread.isInterrupted()) {
            thread.interrupt();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!thread.isInterrupted()) {
            thread.interrupt();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        thread = new Thread(this::getCurrentLocation);
        thread.start();
    }


    public void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(),
                                               Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                                                      Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                                              new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                      Manifest.permission.ACCESS_COARSE_LOCATION}
                    , REQUEST_LOCATION);
        }
        else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                listener = (this::getWeatherInfos);
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1,
                                               listener);
            });

        }
    }


    private void getWeatherInfos(Location location) {

        String apiKey = "ebf300fa0bce8fb61fe75315e514ade9";
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    String.format("https://api.openweathermap.org/data/2" +
                                          ".5/weather?lat=%s&lon=%s&appid=%s",
                                  location.getLatitude(), location.getLongitude(),
                                  apiKey)
            ).openConnection();
            System.setProperty("http.keepAlive", "false");
            if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
                return;
            StringBuilder builder = new StringBuilder();
            new BufferedReader(new InputStreamReader(con.getInputStream())).lines().forEach(builder::append);
            con.disconnect();

            JSONObject json = new JSONObject(builder.toString());


            binding.viewProgress.setText(json.toString());

            manager.removeUpdates(listener);

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

    }

}