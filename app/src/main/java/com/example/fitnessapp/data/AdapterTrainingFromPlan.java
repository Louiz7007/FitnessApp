package com.example.fitnessapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fitnessapp.R;

import java.util.ArrayList;

public class AdapterTrainingFromPlan extends BaseAdapter {

    ArrayList<Training> trainings;
    Context context;


    public AdapterTrainingFromPlan(Context context, ArrayList<Training> trainings) {
        this.context = context;
        this.trainings = trainings;
    }


    @Override
    public int getCount() {
        return trainings.size();
    }

    @Override
    public Object getItem(int position) {
        return trainings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return trainings.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.training_from_trainingplan_item, parent, false);
        }

        Training training = trainings.get(position);

        TextView textViewName = convertView.findViewById(R.id.trainingLVName2);
        TextView textViewIntesity = convertView.findViewById(R.id.trainingIntensityLV2);

        textViewName.setText(training.getName());
        textViewIntesity.setText(training.getIntensity());


        return convertView;
    }
}
