package com.example.fitnessapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

        TextView textViewName = convertView.findViewById(R.id.textLVName);
        TextView textViewIntesity = convertView.findViewById(R.id.textLVIntensity);
        TextView textViewMet = convertView.findViewById(R.id.textViewLVDuration);

        textViewName.setText(currentTraining.getName());
        textViewIntesity.setText(currentTraining.getIntensity());
        textViewMet.setText(String.valueOf(currentTraining.getMetValue()));

        return convertView;
    }
}
