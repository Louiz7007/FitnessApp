package com.example.fitnessapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fitnessapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterTodayTraining extends BaseAdapter {

    Context context;
    ArrayList<TodayTraining> todayTrainingsList;

    public AdapterTodayTraining(Context context, ArrayList<TodayTraining> todayTrainingsList) {
        this.context = context;
        this.todayTrainingsList = todayTrainingsList;
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

        TodayTraining currentTraining = (TodayTraining) getItem(position);

        TextView textViewName = (TextView) convertView.findViewById(R.id.textViewLVName);
        TextView textViewIntesity = (TextView) convertView.findViewById(R.id.textViewLVIntensity);
        TextView textViewMet = (TextView) convertView.findViewById(R.id.textViewLVMetValue);
        TextView textViewSucc = (TextView) convertView.findViewById(R.id.textViewLVSuccess);

        textViewName.setText(currentTraining.getName());
        textViewIntesity.setText(currentTraining.getIntensity());
        textViewMet.setText(String.valueOf(currentTraining.getMetValue()));
        String success = currentTraining.getSuccess() == true ? "Beendet" : "Offen";
        textViewSucc.setText(success);

        return convertView;
    }
}
