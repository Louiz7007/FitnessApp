package com.example.fitnessapp.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.fitnessapp.R;

import java.util.ArrayList;

public class AdapterTrainingWithCheckbox extends BaseAdapter {

    Context context;
    ArrayList<Training> trainingList;
    ArrayList<Training> selectedTrainingList = new ArrayList<>();


    public AdapterTrainingWithCheckbox(Context context, ArrayList<Training> trainingList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_training_withcheckbox, parent,
                    false);
        }

        Training currentTraining = (Training) getItem(position);

        TextView textViewName = convertView.findViewById(R.id.textLVName);
        TextView textViewIntesity = convertView.findViewById(R.id.textLVIntensity);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedTrainingList.add(currentTraining);
                } else {
                    selectedTrainingList.remove(currentTraining);
                }
            }
        });

        textViewName.setText(currentTraining.getName());
        textViewIntesity.setText(currentTraining.getIntensity());



        return convertView;
    }


    public ArrayList<Training> getSelectedTrainingList() {
        return selectedTrainingList;
    }
}