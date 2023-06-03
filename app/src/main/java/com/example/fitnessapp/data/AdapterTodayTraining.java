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
        return todayTrainingsList.get(position);
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

        TextView textViewName = (TextView) convertView.findViewById(R.id.textViewLVName);
        TextView textViewIntensity = (TextView) convertView.findViewById(R.id.textViewLVIntensity);
        TextView textViewDur = (TextView) convertView.findViewById(R.id.textViewLVDuration);
        TextView success = (TextView) convertView.findViewById(R.id.textViewLVSuccess);
        Button changeSuccess = (Button) convertView.findViewById(R.id.buttonChangeSuccess);

        textViewName.setText(currentTraining.getName());
        textViewIntensity.setText(currentTraining.getIntensity());
        textViewDur.setText(String.valueOf(currentTraining.getDuration()));
        String successString = currentTraining.getSuccess() == true ? "Erl." : "Offen";
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
