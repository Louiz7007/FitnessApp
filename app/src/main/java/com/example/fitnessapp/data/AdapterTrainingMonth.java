package com.example.fitnessapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fitnessapp.R;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdapterTrainingMonth extends BaseAdapter {

    Context context;
    private final ArrayList<Training> trainings;

    public AdapterTrainingMonth(Context context, ArrayList<Training> trainings) {
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.monthlist_item, parent,
                    false);
        }

        Training currentTraining = (Training) getItem(position);

        TextView textViewDate = convertView.findViewById(R.id.month_date);
        TextView textViewName = convertView.findViewById(R.id.month_name);
        TextView textViewIntesity = convertView.findViewById(R.id.month_intensity);

        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date date = null;
        try {
            date = sdf.parse(currentTraining.getTimestamp().toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        textViewDate.setText(date.toString());
        textViewName.setText(currentTraining.getName());
        textViewIntesity.setText(currentTraining.getIntensity());

        return convertView;
    }
}
