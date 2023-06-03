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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnessapp.DBOpenHelper;
import com.example.fitnessapp.ProfileActivity;
import com.example.fitnessapp.R;
import com.example.fitnessapp.data.AdapterTodayTraining;
import com.example.fitnessapp.data.AdapterTraining;
import com.example.fitnessapp.data.TodayTraining;
import com.example.fitnessapp.data.TodayTrainingList;
import com.example.fitnessapp.data.Training;
import com.example.fitnessapp.data.TrainingList;
import com.example.fitnessapp.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
        final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        // TEST-Daten TODO LD später entfernen
//        helper.deleteUsertrainings();
//        helper.insertUserTraining(1, sdf3.format(timestamp), 1, false);
//        helper.insertUserTraining(2, sdf3.format(timestamp), 0.5, false);
//        helper.insertUserTraining(3, sdf3.format(timestamp), 1.25, false);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        manager = getActivity().getSystemService(LocationManager.class);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        showDayProgress(homeViewModel);

        final ProgressBar progressBar = binding.progressBarDay;
        homeViewModel.getProgress().observe(getViewLifecycleOwner(), progressBar::setProgress);

        binding.imageButtonArrowLeft.setOnClickListener(v -> {
            showDayProgress(homeViewModel);
        });

        binding.imageButtonArrowRight.setOnClickListener(v -> {
            showWeekProgress(homeViewModel);
        });

        return binding.getRoot();
    }

    public double getMaxValueWeek() {
        double maxValueWeek = 0;
        Integer workoutlevel = helper.selectWorkoutlevel();
        switch (workoutlevel){
            case 1:
                maxValueWeek = 600;
                break;
            case 2:
                maxValueWeek = 3999;
                break;
            case 3:
                maxValueWeek = 7999;
                break;
            case 0:
                maxValueWeek = 10000;
                break;
            default:
                maxValueWeek = 5000;
        }
        return maxValueWeek;
    }

    private void showDayProgress(HomeViewModel viewModel) {

        // 0 = hoch; 3 = mittel; 2 = niedrig; 1 = ungenügend

        double maxValuetoday = getMaxValueWeek() / 7;

        double sum = new TodayTrainingList(helper).getSumOfMetValue();
        int progress = (int) Math.round(sum / maxValuetoday * 100);
        double roundOff = Math.round(maxValuetoday * 100.0) / 100.0;
        viewModel.setmProgress(progress);
        binding.viewProgress.setText(String.format("Tagesfortschritt in MET-Minuten\n(%s / %s)", sum, roundOff));
    }

    private void showWeekProgress(HomeViewModel viewModel) {
        double sum = helper.selectUsertrainingsThisWeek();
        int progress = (int) Math.round(sum / getMaxValueWeek() * 100);
        viewModel.setmProgress(progress);
        binding.viewProgress.setText(String.format("Wochenfortschritt in MET-Minuten \n(%s / %s)", sum, getMaxValueWeek()));
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

        double maxValueWeek = 0;
        Integer workoutlevel = helper.selectWorkoutlevel();
        switch (workoutlevel){
            case 1:
                maxValueWeek = 600;
                break;
            case 2:
                maxValueWeek = 3999;
                break;
            case 3:
                maxValueWeek = 7999;
                break;
            case 4:
                maxValueWeek = 10000;
                break;
            default:
                maxValueWeek = 3999;
        }

        double maxValuetoday = maxValueWeek / 7;

//        binding.progressBarDay.setMax(100);
//        double sum = new TodayTrainingList(helper).getSumOfMetValue();
//        int progress = (int) Math.round(sum / maxValuetoday * 100);
//        binding.progressBarDay.setProgress(progress);
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