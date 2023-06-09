package com.example.fitnessapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.fitnessapp.DBOpenHelper;
import com.example.fitnessapp.R;
import com.example.fitnessapp.ui.home.HomeViewModel;

import java.util.ArrayList;

public class AdapterTodayTraining extends BaseAdapter {

    Context context;
    ArrayList<TodayTraining> todayTrainingsList;
    DBOpenHelper helper;

    public AdapterTodayTraining(Context context, ArrayList<TodayTraining> todayTrainingsList, DBOpenHelper helper) {
        this.context = context;
        this.todayTrainingsList = todayTrainingsList;
        this.helper = helper;
    }

    @Override
    public int getCount() {
        return todayTrainingsList.size();
    }

    @Override
    public Object getItem(int position) {

        if(todayTrainingsList.size() > 0 && !(position >= todayTrainingsList.size())){
            return todayTrainingsList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_today_training, parent,
                                                                false);
        }
        todayTrainingsList = helper.selectTodaysTraining();

        TodayTraining currentTraining = (TodayTraining) getItem(position);
        if(currentTraining == null) {
            return convertView;
        }

        TextView textViewName = convertView.findViewById(R.id.textViewLVName);
        TextView textViewIntensity = convertView.findViewById(R.id.textViewLVIntensity);
        TextView textViewDur = convertView.findViewById(R.id.textViewLVDuration);
        TextView success = convertView.findViewById(R.id.textViewLVSuccess);
        Button changeSuccess = convertView.findViewById(R.id.buttonChangeSuccess);

        textViewName.setText(currentTraining.getName());
        textViewIntensity.setText(currentTraining.getIntensity());
        textViewDur.setText(String.valueOf(currentTraining.getDuration()));
        String successString = currentTraining.getSuccess() ? "Erl." : "Offen";
        success.setText(successString);

        changeSuccess.setOnClickListener(v -> {
            if(currentTraining.getSuccess()) {
                currentTraining.setSuccess(false);
                helper.updateUsertrainingsStatus(currentTraining.getId(), false);
                String newSuccessString = "Offen";
                success.setText(newSuccessString);
            }else {
                currentTraining.setSuccess(true);
                helper.updateUsertrainingsStatus(currentTraining.getId(), true);
                String newSuccessString = "Erl.";
                success.setText(newSuccessString);
            }
        });

        return convertView;
    }
}
