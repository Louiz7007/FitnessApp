package com.example.fitnessapp.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.annotation.Nullable;
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

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final int REQUEST_LOCATION = 12345;
    LocationManager manager;
    private FragmentHomeBinding binding;
    private volatile boolean running;

    private ListView listView;
    private DBOpenHelper helper;
    private Cursor cursor;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DBOpenHelper(getActivity());

        for(int i = 0; i < 15; i++) {
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
        manager = getActivity().getSystemService(LocationManager.class);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        getCurrentLocation();

        cursor = helper.selectTodaysTraining();
        ArrayList<String> trainingList = new ArrayList<>();
        while(cursor.moveToNext()) {
            String success = + cursor.getInt(3) == 0 ? "Offen" : "Beendet";
            trainingList.add(cursor.getString(0)+ " | " + cursor.getString(1) +
                    " | " + cursor.getString(2) + " | " + success);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, trainingList);
        binding.trainingsListView.setAdapter(adapter);
        binding.textHome.setOnClickListener(v -> {
            helper.deleteUserTrainingAndTrainings();
        });

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.running = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.running = true;
        new Thread(this::getCurrentLocation).start();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getCurrentLocation() {
        while (running) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                                                   Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(),
                                                          Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                                                  new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                          Manifest.permission.ACCESS_COARSE_LOCATION}
                        , REQUEST_LOCATION);
            }
            Handler handler = new Handler(Looper.getMainLooper());

            try {

                handler.post(() -> {
                    LocationListener listener = (location -> {
                        getWeatherInfos(location);
                    });
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, listener);
                });
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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

            //getActivity().runOnUiThread(() -> {

            binding.textHome.setText(json.toString());

            //});

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

    }

}