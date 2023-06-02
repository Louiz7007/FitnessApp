package com.example.fitnessapp.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fitnessapp.R;

import java.util.ArrayList;

public class AdapterTraining extends BaseAdapter {

    Context context;
    ArrayList<Training> trainingList;

    public AdapterTraining(Context context, ArrayList<Training> trainingList) {
        this.context = context;
        this.trainingList = trainingList;
    }

    @Override
    public int getCount() {
        return trainingList.size();
    }

    @Override
    public Object getItem(int position) {
        return trainingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_training, parent,
                                                                false);
        }

        Training currentTraining = (Training) getItem(position);

        TextView textViewName = (TextView) convertView.findViewById(R.id.textViewLVName);
        TextView textViewIntesity = (TextView) convertView.findViewById(R.id.textViewLVIntensity);
        TextView textViewMet = (TextView) convertView.findViewById(R.id.textViewLVMetValue);

        textViewName.setText(currentTraining.getName());
        textViewIntesity.setText(currentTraining.getIntensity());
        textViewMet.setText(String.valueOf(currentTraining.getMetValue()));

        return convertView;
    }
}
